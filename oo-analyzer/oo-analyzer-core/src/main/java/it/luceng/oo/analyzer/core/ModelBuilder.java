package it.luceng.oo.analyzer.core;

import java.io.IOException;
import java.io.Writer;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Stack;
import java.util.stream.Collectors;

import it.lucaneg.logutils.EnrichedLogger;
import it.lucaneg.oo.api.analyzer.program.MClass;
import it.lucaneg.oo.api.analyzer.program.MCodeBlock;
import it.lucaneg.oo.api.analyzer.program.MCodeMember;
import it.lucaneg.oo.api.analyzer.program.MConstructor;
import it.lucaneg.oo.api.analyzer.program.MField;
import it.lucaneg.oo.api.analyzer.program.MFormalParameter;
import it.lucaneg.oo.api.analyzer.program.MLocalVariable;
import it.lucaneg.oo.api.analyzer.program.MMethod;
import it.lucaneg.oo.api.analyzer.program.Program;
import it.lucaneg.oo.api.analyzer.program.instructions.ArrayStore;
import it.lucaneg.oo.api.analyzer.program.instructions.Assert;
import it.lucaneg.oo.api.analyzer.program.instructions.BranchingStatement;
import it.lucaneg.oo.api.analyzer.program.instructions.Command;
import it.lucaneg.oo.api.analyzer.program.instructions.FieldWrite;
import it.lucaneg.oo.api.analyzer.program.instructions.LocalAssignment;
import it.lucaneg.oo.api.analyzer.program.instructions.LocalDeclaration;
import it.lucaneg.oo.api.analyzer.program.instructions.Return;
import it.lucaneg.oo.api.analyzer.program.instructions.Skip;
import it.lucaneg.oo.ast.ClassDefinition;
import it.lucaneg.oo.ast.ConstructorDefinition;
import it.lucaneg.oo.ast.FieldDefinition;
import it.lucaneg.oo.ast.FormalDefinition;
import it.lucaneg.oo.ast.MethodDefinition;
import it.lucaneg.oo.ast.expression.Variable;
import it.lucaneg.oo.ast.expression.dereference.ArrayAccess;
import it.lucaneg.oo.ast.expression.dereference.FieldAccess;
import it.lucaneg.oo.ast.statement.CodeBlock;
import it.lucaneg.oo.ast.statement.Statement;
import it.lucaneg.oo.ast.types.ClassType;

public class ModelBuilder {
	
	private static final EnrichedLogger logger = new EnrichedLogger(ModelBuilder.class);

	private final Map<String, MClass> classes;
	
	private Program program;
	
	public ModelBuilder() { 
		this.classes = new HashMap<>();
	}

	public ExitCode buildModel(Collection<ClassDefinition> classes) { 
		program = new ProgramImpl();

		for (ClassDefinition c : logger.mkIterationLogger("Building the program model", "classes").iterate(classes)) {
			MClass clazz = mk(c);
			
			for (FieldDefinition fs : c.getFields()) 
				clazz.addField(new MField(clazz, fs.getLine(), fs.getName(), fs.getType()));
			
			for (ConstructorDefinition cs : c.getConstructors()) {
				MConstructor constructor = new MConstructor(clazz, cs.getLine(), buildParameters(cs.getFormals()));
				clazz.addConstructor(constructor);
				constructor.setCode(new CodeParser(constructor).parseCode(clazz, cs.getCode(), cs.getFormals()));
			}
			
			for (MethodDefinition ms : c.getMethods()) {
				MMethod method = new MMethod(clazz, ms.getLine(), ms.getName(), ms.getReturnType(), buildParameters(ms.getFormals()));
				clazz.addMethod(method);
				method.setCode(new CodeParser(method).parseCode(clazz, ms.getCode(), ms.getFormals()));
			}
		}
		
		return ExitCode.SUCCESS;
	}
	
	private MFormalParameter[] buildParameters(FormalDefinition[] formals) {
		MFormalParameter[] result = new MFormalParameter[formals.length];
		for (int i = 0; i < result.length; i++) 
			result[i] = new MFormalParameter(formals[i].getType(), formals[i].getName());
		return result;
	}
	
	public Program getProgram() {
		return program;
	}

	private MClass mk(ClassDefinition ct) {
		if (classes.containsKey(ct.getName()))
			return classes.get(ct.getName());
		
		MClass superclass = ct.getSuperclass() == null ? null : mk(ct.getSuperclass());
		MClass result = new MClass(ct.getName(), ct.getSource(), superclass);
		if (superclass != null)
			superclass.addAsInstance(result);
		
		classes.put(ct.getName(), result);
		((ProgramImpl) program).addClass(result);
		
		return result;
	}

	public void dumpDotFiles(FileManager manager) {
		logger.mkTimerLogger("Dumpig control flow graphs").execAction(() -> {
			for (MClass entry : classes.values()) 
				if (entry.isObject() || entry.isString()) 
					continue;
				else {
					for (MConstructor constructor : entry.getConstructors())
						try (Writer writer = manager.mkOutputFile("cfg-" + constructor.toStringForFileName() + ".dot")) {
							constructor.getCode().dump(writer, "constructor");
						} catch (IOException e) {
							logger.error("Unable to dump cfg-" + constructor.toStringForFileName() + ".dot", e);
						}
					
					for (MMethod method : entry.getMethods())
						try (Writer writer = manager.mkOutputFile("cfg-" + method.toStringForFileName() + ".dot")) {
							method.getCode().dump(writer, method.getName());
						} catch (IOException e) {
							logger.error("Unable to dump cfg-" + method.toStringForFileName() + ".dot", e);
						}
				}
		});
	}
	
	private static class CodeParser {
		private final MCodeMember codeMember;

		public CodeParser(MCodeMember codeMember) {
			this.codeMember = codeMember;
		}
		
		private static class LocalVariables {
			Stack<Collection<MLocalVariable>> variables = new Stack<>();
			
			void add(MLocalVariable var) {
				variables.peek().add(var);
			}
			
			void addLayer() {
				variables.push(new HashSet<>());
			}
			
			void removeLayer() {
				variables.pop();
			}
			
			Collection<MLocalVariable> all() {
				return variables.stream().flatMap(c -> c.stream()).collect(Collectors.toList());
			}
			
			MLocalVariable find(Variable var) {
				Optional<MLocalVariable> result = variables.stream().flatMap(c -> c.stream()).filter(v -> v.getName().equals(var.getName())).findAny();
				if (result.isPresent())
					return result.get();
				
				throw new IllegalStateException("Variable " + var.getName() + " not defined");
			}
		}
		
		private MCodeBlock parseCode(MClass clazz, CodeBlock body, FormalDefinition[] formalDefinitions) {
			LocalVariables variables = new LocalVariables();
			variables.addLayer();
			
			for (FormalDefinition def : formalDefinitions)
				variables.add(new MLocalVariable(def.getType(), def.getName()));
			
			variables.add(new MLocalVariable(ClassType.get(clazz.getName()), "this"));
			if (!clazz.isObject())
				variables.add(new MLocalVariable(ClassType.get(clazz.getSuperclass().getName()), "super"));
			
			if (body.getStatements().length == 0)
				return parseEmptyMethod(variables);
			else
				return parseCode(body, variables);
		}
		
		private MCodeBlock parseEmptyMethod(LocalVariables variables) {
			MCodeBlock result = new MCodeBlock();
			result.add(new Return(codeMember, codeMember.getLine(), 0, variables.all(), null));
			return result;
		}
		
		private MCodeBlock parseCode(CodeBlock body, LocalVariables variables) {
			MCodeBlock result = new MCodeBlock();
			
			variables.addLayer();
			
			for (Statement st : body.getStatements())
				result.concat(parseStatement(st, variables));
			
			variables.removeLayer();
			
			return result;
		}
		
		private MCodeBlock parseStatement(Statement body, LocalVariables variables) {
			if (body == null)
				return null;
			
			MCodeBlock result = new MCodeBlock();
				
			if (body instanceof it.lucaneg.oo.ast.statement.For) 
				_for(result, body, variables);
			else if (body instanceof it.lucaneg.oo.ast.statement.While) 
				_while(result, body, variables);
			else if (body instanceof it.lucaneg.oo.ast.statement.If) 
				ifthenelse(result, body, variables);
			else if (body instanceof it.lucaneg.oo.ast.statement.Return) 
				_return(result, body, variables);
			else if (body instanceof it.lucaneg.oo.ast.statement.Assert) 
				_assert(result, body, variables);
			else if (body instanceof it.lucaneg.oo.ast.statement.LocalDeclaration) 
				localDeclaration(result, body, variables);
			else if (body instanceof it.lucaneg.oo.ast.statement.Assignment) 
				assignment(result, body, variables);
			else if (body instanceof it.lucaneg.oo.ast.statement.Nop) 
				skip(result, body, variables);
			else if (body instanceof it.lucaneg.oo.ast.statement.CodeBlock) 
				codeBlock(result, body, variables);
			else if (body instanceof it.lucaneg.oo.ast.statement.Command) 
				command(result, body, variables);
			else 
				throw new IllegalArgumentException("Unknown command type: " + body.getClass().getName());
			
			return result;
		}
		
		private void command(MCodeBlock result, Statement body, LocalVariables variables) {
			it.lucaneg.oo.ast.statement.Command command = (it.lucaneg.oo.ast.statement.Command) body;
			result.add(new Command(codeMember, command.getLine(), command.getPos(), variables.all(), command.getExpression()));
		}
		
		private void _for(MCodeBlock result, Statement body, LocalVariables variables) {
			it.lucaneg.oo.ast.statement.For _for = (it.lucaneg.oo.ast.statement.For) body;
			
			int line = _for.getLine();
			int pos = _for.getPos();
			
			MCodeBlock init = parseStatement(_for.getInitialisation(), variables);
			
			BranchingStatement branch = new BranchingStatement(codeMember, line, pos, variables.all(), _for.getCondition());
			
			MCodeBlock loopBody = parseStatement(_for.getBody(), variables);
			loopBody.concat(parseStatement(_for.getUpdate(), variables));
			
			Skip join = new Skip(codeMember, line, pos, variables.all());
			
			result.concat(init); 
			result.addLoop(branch, loopBody, join);
		}
		
		private void _while(MCodeBlock result, Statement body, LocalVariables variables) {
			it.lucaneg.oo.ast.statement.While _while = (it.lucaneg.oo.ast.statement.While) body;
			
			int line = _while.getLine();
			int pos = _while.getPos();
			
			BranchingStatement branch = new BranchingStatement(codeMember, line, pos, variables.all(), _while.getCondition());

			MCodeBlock loopBody = parseStatement(_while.getBody(), variables);
			
			Skip join = new Skip(codeMember, line, pos, variables.all());
			
			result.addLoop(branch, loopBody, join);
		}
		
		private void ifthenelse(MCodeBlock result, Statement body, LocalVariables variables) {
			it.lucaneg.oo.ast.statement.If _if = (it.lucaneg.oo.ast.statement.If) body;
			
			int line = _if.getLine();
			int pos = _if.getPos();
			
			BranchingStatement branch = new BranchingStatement(codeMember, line, pos, variables.all(), _if.getCondition());
			
			MCodeBlock ifTrue = parseStatement(_if.getThen(), variables);
			
			MCodeBlock ifFalse = parseStatement(_if.get_else(), variables);
			
			Skip join = new Skip(codeMember, line, pos, variables.all());
			
			result.addBranch(branch, ifTrue, ifFalse, join);
		}
		
		private void _return(MCodeBlock result, Statement body, LocalVariables variables) {
			it.lucaneg.oo.ast.statement.Return ret = (it.lucaneg.oo.ast.statement.Return) body;
			result.add(new Return(codeMember, ret.getLine(), ret.getPos(), variables.all(), ret.getReturned()));
		}

		private void assignment(MCodeBlock result, Statement body, LocalVariables variables) {
			it.lucaneg.oo.ast.statement.Assignment assignment = (it.lucaneg.oo.ast.statement.Assignment) body;
			if (assignment.getTarget() instanceof Variable) {
				Variable var = (Variable) assignment.getTarget();
				result.add(new LocalAssignment(codeMember, assignment.getLine(), assignment.getPos(), variables.all(), variables.find(var), assignment.getExpression())); 
			} else if (assignment.getTarget() instanceof FieldAccess) {
				FieldAccess field = (FieldAccess) assignment.getTarget();
				result.add(new FieldWrite(codeMember, assignment.getLine(), assignment.getPos(), variables.all(), field, assignment.getExpression())); 
			} else if (assignment.getTarget() instanceof ArrayAccess) {
				ArrayAccess array = (ArrayAccess) assignment.getTarget();
				result.add(new ArrayStore(codeMember, assignment.getLine(), assignment.getPos(), variables.all(), array, assignment.getExpression())); 
			} else
				throw new IllegalArgumentException("Cannot assign a value to " + assignment.getTarget().getClass().getName());
		}

		private void codeBlock(MCodeBlock result, Statement body, LocalVariables variables) {
			it.lucaneg.oo.ast.statement.CodeBlock block = (it.lucaneg.oo.ast.statement.CodeBlock) body;
			result.concat(parseCode(block, variables));
		}

		private void localDeclaration(MCodeBlock result, Statement body, LocalVariables variables) {
			it.lucaneg.oo.ast.statement.LocalDeclaration local = (it.lucaneg.oo.ast.statement.LocalDeclaration) body;
			MLocalVariable var = new MLocalVariable(local.getType(), local.getName());
			variables.add(var);
			result.add(new LocalDeclaration(codeMember, local.getLine(), local.getPos(), variables.all(), var, local.getInitialization()));
		}

		private void _assert(MCodeBlock result, Statement body, LocalVariables variables) {
			it.lucaneg.oo.ast.statement.Assert ass = (it.lucaneg.oo.ast.statement.Assert) body;
			result.add(new Assert(codeMember, ass.getLine(), ass.getPos(), variables.all(), ass.getExpression()));
		}
		
		private void skip(MCodeBlock result, Statement body, LocalVariables variables) {
			it.lucaneg.oo.ast.statement.Nop skip = (it.lucaneg.oo.ast.statement.Nop) body;
			result.add(new Skip(codeMember, skip.getLine(), skip.getPos(), variables.all()));
		}
	}
}
