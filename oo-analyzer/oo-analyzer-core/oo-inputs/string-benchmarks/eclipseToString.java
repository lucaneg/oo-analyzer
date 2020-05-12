class EclipseToString {
	String dump(Person[] people) {
		String result = "People: {\n";
		
		for (int i = 0; i < this.length(people); i = i + 1) {
			Person person = people[i];
			
			result = result.concat("\tPerson[name=");
			String name = person.getName();
			result = result.concat(name);
			
			result = result.concat(", age=");
			int age = person.getAge();
			result = result.concat(age);

			if (i == this.length(people) - 1)
				result = result.concat("]\n");
			else
				result = result.concat("],\n");
		}
		result = result.concat("}");
		
		return result;
	}
}