package sintatico;


class simbolo{

    public enum Tipo{
        INTEIRO, FLOAT, STING;
    }

    private String var;
    private Tipo tipo;
    private String valor;

    simbolo(String var, Tipo tipo, String valor){
        this.var = var;
        this.tipo = tipo;
        this.valor = valor;
    }

    public String getVarName(){
        return this.var;
    }



    
}