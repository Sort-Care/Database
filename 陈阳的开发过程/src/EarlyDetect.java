

public class EarlyDetect {
	public static void main(String[] args) {
		System.out.println(createDetect("CREATE TABLE ACCOUNT(ACCOUNT_NUMBER CHAR, BRANCH_NAME CHAR, BALANCE TEXT, "
				+ "PRIMARY KEY(ACCOUNT_NUMBER))"));
	}
	
	public static boolean selectDetect(String span){
		span = span.toUpperCase();
		//System.out.println(span);
		String column = "(\\w+\\s*(\\w+\\s*){0,1})";// 一列的正则表达式 匹配如 product p
		String columns = column + "(,\\s*" + column + ")*"; // 多列正则表达式
		String ownerenable = "((\\w+\\.){0,1}\\w+\\s*(\\w+\\s*){0,1})";// 一列的正则表达式
		String ownerenables = ownerenable + "(,\\s*" + ownerenable + ")*";// 多列正则表达式
		String from = "FROM\\s+" + columns;
		String condition = "\\s*(\\w+\\.){0,1}\\w+\\s*(=|LIKE|IS)\\s*'?(\\w+\\.){0,1}[\\w%]+'?";// 条件的正则表达式
		String conditions = condition + "(\\s+(AND|OR)\\s*" + condition
				+ "\\s*)*";// 多个条件 匹配如 a=b and c like 'r%' or d is null
		String where = "(WHERE\\s+" + conditions + "){0,1}";
		String pattern = "SELECT\\s+(\\*|" + ownerenables + ")"+ "\\s+" + "(" + from
				+ ")\\s+" + where + "\\s*"; // 匹配最终sql的正则表达式
		return span.matches(pattern);
	}
	
	public static boolean createDetect(String span){
		//create table tabname(coll typel [not null] [primary key], col2 type2[not null]...)
		span = span.toUpperCase();
		String types = "CHAR|VARCHAR|TEXT|INT|DOUBLE|DATE|DATETIME";
		String columns = "((\\s*\\w+\\s+)(" + types + ")(\\s+NOT\\s+NULL|)\\s*\\,\\s*)+";
		String primaryKey = "PRIMARY\\s+KEY\\s*\\(\\s*\\w+\\s*\\)\\s*";
		String foreignKey = "(\\s*\\,\\s*FOREIGN\\s+KEY\\s*\\(\\s*\\w+\\s*\\)\\s*REFERENCES\\s+\\w+\\s*){0,}";
		String create = "CREATE\\s+TABLE\\s+\\w+\\s*\\(" + columns + primaryKey + foreignKey + "\\)\\s*";
		return span.matches(create);
	}
}
