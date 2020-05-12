package it.lucaneg.oo.parser;

import java.io.File;
import java.io.FileInputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import org.antlr.v4.runtime.BailErrorStrategy;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.FailedPredicateException;
import org.antlr.v4.runtime.InputMismatchException;
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.LexerNoViableAltException;
import org.antlr.v4.runtime.NoViableAltException;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.Vocabulary;
import org.antlr.v4.runtime.atn.PredictionMode;
import org.antlr.v4.runtime.misc.IntervalSet;

import it.lucaneg.antlr.JavaSubsetLexer;
import it.lucaneg.antlr.JavaSubsetParser;
import it.lucaneg.logutils.EnrichedLogger;
import it.lucaneg.oo.ast.ClassDefinition;
import it.lucaneg.oo.parser.typecheck.TypeCheckException;
import it.lucaneg.oo.parser.typecheck.TypeChecker;

public class OOParser {

	private static final EnrichedLogger logger = new EnrichedLogger(OOParser.class);

	public static void main(String[] args) throws ParsingException, TypeCheckException {
		File f = new File(args[0]);
		
		logger.info("Parsing all files in " + f);
		File[] allFiles = f.listFiles(javaFileFilter());
		Collection<ClassDefinition> classes = OOParser.parseFiles(allFiles);
		for (ClassDefinition clazz : classes)
			System.out.println(clazz.fullDump());
	}

	private static FilenameFilter javaFileFilter() {
		return new FilenameFilter() {
			@Override
			public boolean accept(File dir, String name) {
				return name.endsWith(".java");
			}
		};
	}
	
	public static Collection<ClassDefinition> parseFiles(File[] allFiles) throws ParsingException, TypeCheckException { 
		Collection<ClassDefinition> result = new HashSet<>();
		for (File file : allFiles) 
			result.add(parseSingleFile(file));
		
		new TypeChecker(result).typeCheck();
		
		return result;
	}

	private static ClassDefinition parseSingleFile(File file) throws ParsingException {
		try (FileInputStream input = new FileInputStream(file)) {
			Lexer lexer = new JavaSubsetLexer(CharStreams.fromStream(input));
			CommonTokenStream tokens = new CommonTokenStream(lexer);
			JavaSubsetParser parser = new JavaSubsetParser(tokens);
			parser.setErrorHandler(new BailErrorStrategy());
			parser.getInterpreter().setPredictionMode(PredictionMode.SLL);

			return (ClassDefinition) new ASTVisitor(file.getName()).visit(parser.classDeclaration());
		} catch (RecognitionException e) {
			handleRecognitionException(file, e);
		} catch (Exception e) {
			if (e.getCause() instanceof RecognitionException)
				handleRecognitionException(file, (RecognitionException) e.getCause());
			else {
				logger.error("Parser thrown an exception while parsing " + file.getName(), e);
				throw new ParsingException("Parser thrown an exception while parsing " + file.getName(), e);
			}
		}

		return null;
	}

	private static void handleRecognitionException(File file, RecognitionException e) throws ParsingException {
		logger.error("Error while parsing " + file.getName() + ":\n" + createMessage(e, file, true));
		throw new ParsingException("Error while parsing " + file.getName() + ":\n" + createMessage(e, file, false));
	}

	private static String createMessage(RecognitionException e, File file, boolean dumpLine) {
		Token problem = e.getOffendingToken();
		StringBuilder message = errorHeader(file, problem);

		if (e instanceof InputMismatchException)
			inputMismatch(e, problem, message);
		else if (e instanceof FailedPredicateException)
			failedPredicate(e, message);
		else if (e instanceof NoViableAltException || e instanceof LexerNoViableAltException)
			missingAlternative(message);
		else
			message.append("an unknown error occurred");

		if (!dumpLine)
			return message.toString();

		dumpProblem(file, problem, message);

		return message.toString();
	}

	private static void dumpProblem(File file, Token problem, StringBuilder message) {
		String base = message.toString();

		message.append(" at: ");
		int begin = message.length();

		try {
			List<String> inputLines = Files.readAllLines(file.toPath());
			String errorLine = inputLines.get(problem.getLine() - 1);
			String trimmed = errorLine.trim();
			int offset = Math.max(0, errorLine.indexOf(trimmed));
			message.append(trimmed).append("\n");
			for (int i = 0; i < begin + problem.getCharPositionInLine() - offset; i++)
				message.append(" ");
			message.append("^");
		} catch (IOException e) {
			// something went wrong, roll back
			message.delete(0, message.length());
			message.append(base);
		}
	}

	private static void missingAlternative(StringBuilder message) {
		message.append("could not decide what path to take");
	}

	private static void failedPredicate(RecognitionException e, StringBuilder message) {
		message.append("failed predicate '").append(((FailedPredicateException) e).getPredicate()).append("' ");
	}

	private static void inputMismatch(RecognitionException e, Token problem, StringBuilder message) {
		message.append("matched '").append(problem.getText()).append("' as <")
				.append(tokenName(problem.getType(), e.getRecognizer().getVocabulary())).append(">, expecting <")
				.append(tokenNames(((InputMismatchException) e).getExpectedTokens(), e.getRecognizer().getVocabulary()))
				.append(">");
	}

	private static StringBuilder errorHeader(File file, Token problem) {
		return new StringBuilder().append(file.getName()).append(":").append(problem.getLine()).append(":")
				.append(problem.getCharPositionInLine()).append(" - ");
	}

	private static String tokenName(int type, Vocabulary vocabulary) {
		return type < 0 ? "<EOF>" : vocabulary.getSymbolicName(type);
	}

	private static String tokenNames(IntervalSet types, Vocabulary vocabulary) {
		List<Integer> typeList = types.toList();
		StringBuilder expectedBuilder = new StringBuilder();

		for (Integer t : typeList)
			expectedBuilder.append(tokenName(t, vocabulary)).append(" ");

		return expectedBuilder.toString().trim();
	}
}
