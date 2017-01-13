package es.me.parser.javaparser;

import java.io.File;
import java.io.FileNotFoundException;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.comments.Comment;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import es.me.parser.javaparser.support.DirExplorer;
import es.me.parser.javaparser.utils.GetSet;

/**
 * Parser in order to classify methods in a project class using 3 criteria:
 * <ul>
 * 	<li>Calls to a function called "addCallback"</li>
 * 	<li>Number of lines excluding getter and setter methods</li>
 * 	<li>Number of TODO comments</li>
 * </ul>
 *	@author igayolfernan
 */
public class Parser {
	
	private static int totalOneAddCallback = 0;
	private static int totalTwoAddCallback = 0;
	private static int totalThreeOrMoreAddCallback = 0;
	
	private static int totalMethodsUnder50Lines = 0;
	private static int totalMethodsUnder150Lines = 0;
	private static int totalMethodsOver150Lines = 0;
	
	private static int lessThan3Todos = 0;
	private static int between4And7Todos = 0;
	private static int morethan7Todos = 0;
	
	/**
	 * Crawls all the methods in all the classes of a project dir in order 
	 * to extract information of root methods in classes found
	 * 
	 * @param projectDir <code>File</code> Project directory
	 */
	public static void crawlMethodDefinitions (File projectDir) {
		new DirExplorer((level, path, file) -> path.endsWith(".java"), (level, path, file) -> {
			try{
				new MethodVisitor().visit(JavaParser.parse(file), null);
			} catch (Exception e) {
                new RuntimeException(e);
            }
		}).explore(projectDir);
	}
	
	
    public static void main( String[] args ) throws FileNotFoundException
    {
       //FileInputStream in = new FileInputStream("C:\\Users\\igayolfernan\\Desktop\\CSC\\Proyectos\\Societe_General\\PMD\\Code\\RFD\\src\\java\\com\\socgen\\rfd\\ihm\\controller\\CtrlMvtController.java");
       // parse the file
       //CompilationUnit cu = JavaParser.parse(in);
       //new MethodVisitor().visit(cu, null);
    
       File projectDir = null;
       if (args.length == 0){
    	   projectDir = new File("C:\\Users\\igayolfernan\\Desktop\\CSC\\Proyectos\\Societe_General\\PMD\\Code\\RFD\\src\\java\\com\\socgen\\rfd\\ihm\\controller");
       } else {
    	   projectDir = new File(args[0]);
       }
       crawlMethodDefinitions(projectDir);
       printCallbackTotals();
       printLineTotals();
       printTodoTotals();
    }
    
    /**
     * Visitor implementation for visiting MethodDeclaration nodes.
     */
    private static class MethodVisitor extends VoidVisitorAdapter<Void> {
    	
        @Override
        public void visit(MethodDeclaration n, Void arg) {
	        
        	/* here you can access the attributes of the method.
	         this method will be called for all methods in this 
	         CompilationUnit, including inner class methods */
            
            if (!n.getAncestorOfType(MethodDeclaration.class).isPresent()){
            	updateLinesPerMethod(n);
            	updateAddCallback(n);
            	updateTodoComments(n);
            }

            super.visit(n, arg); 
 
        }
        
        /**
         * Internal utility function in order to count the number of lines of one method.
         * 
         * Getter and setter methods are excluded.
         * 
         * @param n <code>MethodDeclaration</code> JavaParser method declaration
         */
        private void updateLinesPerMethod(MethodDeclaration n) {
        	
        	if (!isGetterOrSetter(n)){        		
        	
        	
	        	int begin = n.getBegin().get().line;
	        	int end = n.getEnd().get().line;
	        	int totalLines = end-begin;
	        	
	        	if (totalLines <=50){
	        		totalMethodsUnder50Lines++;
	        	} else if (totalLines <=150) {
					totalMethodsUnder150Lines++;
				} else {
					totalMethodsOver150Lines++;
				}
	        	
        	}
        	
        }

		
        
        /**
         * Internal utility function in order to count the number of calls to the method addCallback.
         * 
         * @param n <code>MethodDeclaration</code> JavaParser method declaration
         */
		private void updateAddCallback(MethodDeclaration n) {
			
			long callbacKCount = n.getNodesByType(MethodCallExpr.class)
					.stream()
					.filter(f -> f.toString().contains("addCallback(new Callback")).count();
         
			
			if( callbacKCount != 0){
     	   
				//        	   System.out.println(n.getName());
				//        	   n.getNodesByType(MethodCallExpr.class).stream()
				//        	   	.filter(f -> f.toString().contains("addCallback(new Callback"))
				//        	   	.forEach(f -> registerAddCallback(n, f, f.getBegin().get().line));
				if (callbacKCount == 1) {
					totalOneAddCallback++;
				} else if (callbacKCount >= 3) {
					totalThreeOrMoreAddCallback++;
				} else {
					totalTwoAddCallback++;
				}
			   
			}
		}
    }
    
    /**
     * Internal utility function in order to count the number of calls to the method addCallback.
     * 
     * @param n <code>MethodDeclaration</code> JavaParser method declaration
     */
	private static void updateTodoComments(MethodDeclaration n) {
		
		if (!isGetterOrSetter(n)){
			long todoCommentCount = n.getAllContainedComments()
					.stream()
					.filter(c -> c.getContent().contains("TODO"))
					.count();
			
			if (todoCommentCount >0){
				if (todoCommentCount <= 3) {
					lessThan3Todos++;
				} else if (todoCommentCount > 7) {
					morethan7Todos++;
				} else {
					between4And7Todos++;
				}
			}
		}
		
	}
    
	private static boolean isGetterOrSetter(MethodDeclaration n) {
		return n.getNameAsString().startsWith(GetSet.GET.getValue())
    			|| n.getNameAsString().startsWith(GetSet.SET.getValue())
    			|| n.getNameAsString().startsWith(GetSet.IS.getValue());
	}
	
    private static void printCallbackTotals(){
    	System.out.println("Total number of methods with:");
    	System.out.println("=============================");
    	System.out.println("One call to addCallback   ->            " + totalOneAddCallback);
    	System.out.println("Two calls to addCallback  ->            " + totalTwoAddCallback);
    	System.out.println("Three or more calls to addCallback  ->  " + totalThreeOrMoreAddCallback);
    	System.out.println();
    }
    
    private static void printLineTotals(){
    	System.out.println("Total number of methods with:");
    	System.out.println("=============================");
    	System.out.println("50 or less lines of code          ->            " + totalMethodsUnder50Lines);
    	System.out.println("Between 50 and 150 lines of code  ->            " + totalMethodsUnder150Lines);
    	System.out.println("Over 150 lines of code            ->            " + totalMethodsOver150Lines);
    	System.out.println();
    }
    
    private static void printTodoTotals(){
    	System.out.println("Total number of methods with:");
    	System.out.println("=============================");
    	System.out.println("3 or less todo comments       ->            " + lessThan3Todos);
    	System.out.println("Between 4 and 7 todo comments ->            " + between4And7Todos);
    	System.out.println("Over 7 lines of todo comments ->            " + morethan7Todos);
    	System.out.println();
    }
    
}
