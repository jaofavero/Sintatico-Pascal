package compiladores.lexico;


public class Palavras {
    private static String[] words = {"and", "array", "begin", "case", "const", "div", "do", "downto", "else", "end",
    "file", "for", "function", "goto", "if", "in", "label", "mod", "nil", "not", "of", "or","packed", "procedure", "program", 
    "record", "repeat", "set","then", "to","type", "until", "var", "while", "with", "integer", "read", "write", "writeln"};

    public static boolean compare(String str){
        for (String string : words) {
            if(str.toLowerCase().equals(string)){
                return true;
            }
        }
        return false;
    }
}
