package compiladores.sintatico;

import compiladores.lexico.Classe;
import compiladores.lexico.Lexico;
import compiladores.lexico.Token;

public class Sintatico {

    private Lexico lexico;
    private Token token;

    public Sintatico(Lexico lexico) {
        this.lexico = lexico;
    }

    public void analisar() {
        token = lexico.nextToken();
        programa();
    }

    // <programa> ::= program <id> {A01} ; <corpo> • {A45}
    private void programa() {
        if (palavraReservada(token, "program")) {
            token = lexico.nextToken();
            if (token.getClasse() == Classe.cId) {
                token = lexico.nextToken();
                // {A01}
                if (token.getClasse() == Classe.cPontoVirg) {
                    token = lexico.nextToken();
                    corpo();
                    if (token.getClasse() == Classe.cPonto) {
                        token = lexico.nextToken();
                        // {A45}
                    } else {
                        System.out.println(token.getLinha() + "," + token.getColuna() + ", Ponto Final (.) esperado");
                    }
                } else {
                    System.out.println(token.getLinha() + "," + token.getColuna() + ", Ponto e vírgula (;) 2 esperado");
                }
            } else {
                System.out.println(token.getLinha() + "," + token.getColuna() + ", Nome do Programa esperado");
            }
        } else {
            System.out.println(token.getLinha() + "," + token.getColuna() + ", program esperado");
        }
    }

    // <corpo> ::= <declara> <rotina> {A44} begin <sentencas> end {A46}
    private void corpo() {
        declara();
        // rotina();
        // {A44}
        if (palavraReservada(token, "begin")) {
            token = lexico.nextToken();
            sentencas();
            if (palavraReservada(token, "end")) {
                token = lexico.nextToken();
                // {A46}
            } else {
                System.out.println(token.getLinha() + "," + token.getColuna() + ", end esperado");
            }
        } else {
            System.out.println(token.getLinha() + "," + token.getColuna() + ", begin esperado");
        }
    }

    // <declara> ::= var <dvar> <mais_dc> | ε
    private void declara() {
        if (palavraReservada(token, "var")) {
            token = lexico.nextToken();
            dvar();
            mais_dc();
        }
    }

    // <mais_dc> ::= ; <cont_dc>

    private void mais_dc() {
        if (token.getClasse() == Classe.cPontoVirg) {
            token = lexico.nextToken();
            cont_dc();
        }
    }

    // <cont_dc> ::= <dvar> <mais_dc> | ε
    private void cont_dc() {
        if (token.getClasse() == Classe.cId) {
            dvar();
            mais_dc();
        }

    }

    private void dvar() {
        variaveis();
        if (token.getClasse() == Classe.cDoisPontos) {
            token = lexico.nextToken();
            tipo_var();
        } else {
            System.out.println(token.getLinha() + "," + token.getColuna() + ", (:) esperado");
        }

    }

    // <tipo_var> ::= integer
    private void tipo_var() {
        if (palavraReservada(token, "integer")) {
            token = lexico.nextToken();
        } else {
            System.out.println(token.getLinha() + "," + token.getColuna() + ", integer esperado");
        }
    }

    // <variaveis> ::= <id> {A03} <mais_var>
    private void variaveis() {
        if (token.getClasse() == Classe.cId) {
            token = lexico.nextToken();
            // {A03}
            mais_var();
        } else {
            System.out.println(token.getLinha() + "," + token.getColuna() + ", Nome esperado");
        }

    }

    // <mais_var> ::= , <variaveis> | ε
    private void mais_var() {
        if (token.getClasse() == Classe.cVirg) {
            token = lexico.nextToken();
            variaveis();
        }

    }

    // <sentencas> ::= <comando> <mais_sentencas>
    private void sentencas() {
        comando();
        mais_sentencas();
    }

    // <mais_sentencas> ::= ; <cont_sentencas>
    private void mais_sentencas() {
        if (token.getClasse() == Classe.cPontoVirg) {
            token = lexico.nextToken();
            cont_sentencas();
        } else {
            System.out.println(token.getLinha() + "," + token.getColuna() + ", (;) 1 esperado");
        }

    }

    // <cont_sentencas> ::= <sentencas> | ε
    private void cont_sentencas() {
        if (((token.getClasse() == Classe.cPalavraReservada) &&
                (palavraReservada(token, "read")
                        || palavraReservada(token, "write")
                        || palavraReservada(token, "writeln")
                        || palavraReservada(token, "for")
                        || palavraReservada(token, "repeat")
                        || palavraReservada(token, "while")
                        || palavraReservada(token, "if")))
                || (token.getClasse() == Classe.cId)) {
            sentencas();
        }
    }

    // <var_read> ::= <id> {A08} <mais_var_read>
    private void var_read() {
        if (token.getClasse() == Classe.cId) {
            token = lexico.nextToken();
            // {AO8}
            mais_var_read();
        } else {
            System.out.println(token.getLinha() + "," + token.getColuna() + ", Nome esperado");
        }
    }

    // <mais_var_read> ::= , <var_read> | ε
    private void mais_var_read() {
        if (token.getClasse() == Classe.cId) {
            token = lexico.nextToken();
            var_read();
        }
    }

    // <exp_write> ::= <id> {A09} <mais_exp_write> | <string> {A59} <mais_exp_write>
    // | <intnum> {A43} <mais_exp_write>
    private void exp_write() {
        if ((token.getClasse() == Classe.cId)) {
            token = lexico.nextToken();
            // {A09}
            mais_exp_write();
        } else if ((token.getClasse() == Classe.cString)) {
            token = lexico.nextToken();
            // {A59}
            mais_exp_write();
        } else if ((token.getClasse() == Classe.cInt)) {
            token = lexico.nextToken();
            // {A43}
            mais_exp_write();
        } else {
            System.out.println(token.getLinha() + "," + token.getColuna() + ", Esperado (nome), (int), ou (string)");
        }
    }

    // <mais_exp_write> ::= , <exp_write> | ε
    private void mais_exp_write() {
        if (token.getClasse() == Classe.cVirg) {
            token = lexico.nextToken();
            exp_write();
        }
    }

    // comando> ::=
    // read ( <var_read> ) |
    // write ( <exp_write> ) |
    // writeln ( <exp_write> ) {A61} |
    // for <id> {A57} := <expressao> {A11} to <expressao> {A12} do begin <sentencas>
    // end {A13} |
    // repeat {A14} <sentencas> until ( <expressao_logica> ) {A15} |
    // while {A16} ( <expressao_logica> ) {A17} do begin <sentencas> end {A18} |
    // if ( <expressao_logica> ) {A19} then begin <sentencas> end {A20} <pfalsa>
    // {A21} |
    // <id> {A49} := <expressao> {A22}
    // | ε
    private void comando() {
        if (palavraReservada(token, "read")) {
            token = lexico.nextToken();
            if (token.getClasse() == Classe.cParEsq) {

                token = lexico.nextToken();
                var_read();
                if (token.getClasse() == Classe.cParDir) {
                    token = lexico.nextToken();
                } else {
                    System.out.println(token.getLinha() + "," + token.getColuna() + ", ) esperado");
                }
            } else {
                System.out.println(token.getLinha() + "," + token.getColuna() + ", ( esperado");
            }
        } else if (palavraReservada(token, "write")) {
            token = lexico.nextToken();
            if (token.getClasse() == Classe.cParEsq) {
                token = lexico.nextToken();
                exp_write();
                if (token.getClasse() == Classe.cParDir) {
                    token = lexico.nextToken();
                } else {
                    System.out.println(token.getLinha() + "," + token.getColuna() + ", ) esperado");
                }
            } else {
                System.out.println(token.getLinha() + "," + token.getColuna() + ", ( esperado");
            }
        } else if (palavraReservada(token, "writeln")) {
            token = lexico.nextToken();
            if (token.getClasse() == Classe.cParEsq) {
                token = lexico.nextToken();
                exp_write();
                if (token.getClasse() == Classe.cParDir) {
                    token = lexico.nextToken();
                } else {
                    System.out.println(token.getLinha() + "," + token.getColuna() + ", ) esperado");
                }
            } else {
                System.out.println(token.getLinha() + "," + token.getColuna() + ", ( esperado");
            }
            // {A61}
            // for <id> {A57} := <expressao> {A11} to <expressao> {A12} do begin <sentencas>
            // end {A13}
        } else if (palavraReservada(token, "for")) {
            token = lexico.nextToken();
            if (token.getClasse() == Classe.cId) {
                // {A57}
                token = lexico.nextToken();
                // for <id> {A57} := <expressao> {A11} to <expressao> {A12} do begin <sentencas>
                // end {A13} |
                if (token.getClasse() == Classe.cAtribuicao) {
                    token = lexico.nextToken();
                    expressao();
                    // {A11}

                    if (palavraReservada(token, "to")) {
                        token = lexico.nextToken();
                        expressao();
                        // {A12}
                        if (palavraReservada(token, "do")) {
                            token = lexico.nextToken();
                            if (palavraReservada(token, "begin")) {
                                token = lexico.nextToken();
                                // <sentencas> end {A13}
                                sentencas();
                                if (palavraReservada(token, "end")) {
                                    token = lexico.nextToken();
                                    // {A13}
                                } else {
                                    System.out.println(token.getLinha() + "," + token.getColuna() + ", (end) esperado");
                                }
                            } else {
                                System.out.println(token.getLinha() + "," + token.getColuna() + ", (begin) esperado");
                            }
                        } else {
                            System.out.println(token.getLinha() + "," + token.getColuna() + ", (do) esperado");
                        }
                    } else {
                        System.out.println(token.getLinha() + "," + token.getColuna() + ", (to) esperado");
                    }
                } else {
                    System.out.println(token.getLinha() + "," + token.getColuna() + ", (:=) esperado");
                }
            } else {
                System.out.println(token.getLinha() + "," + token.getColuna() + ", (id) esperado");
            }
            // repeat {A14} <sentencas> until ( <expressao_logica> ) {A15}
        } else if (palavraReservada(token, "repeat")) {
            // {A14}
            token = lexico.nextToken();
            sentencas();
            if (palavraReservada(token, "until")) {
                token = lexico.nextToken();
                if (token.getClasse() == Classe.cParEsq) {
                    token = lexico.nextToken();
                    expressao_logica();
                    // {A15}
                    if (token.getClasse() == Classe.cParDir) {
                        token = lexico.nextToken();
                    } else {
                        System.out.println(token.getLinha() + "," + token.getColuna() + ", ) esperado");
                    }
                } else {
                    System.out.println(token.getLinha() + "," + token.getColuna() + ", ( esperado");
                }
            } else {
                System.out.println(token.getLinha() + "," + token.getColuna() + ", until esperado");
            }

        } else if (palavraReservada(token, "while")) {
            // while {A16} ( <expressao_logica> ) {A17} do begin <sentencas> end {A18} |
            token = lexico.nextToken();
            if (token.getClasse() == Classe.cParEsq) {
                token = lexico.nextToken();
                expressao_logica();
                // {A17}
                if (token.getClasse() == Classe.cParDir) {
                    token = lexico.nextToken();
                    //{A17}
                    if (palavraReservada(token, "do")) {
                        token = lexico.nextToken();
                        // <sentencas> end {A18}
                        if (palavraReservada(token, "begin")) {
                            token = lexico.nextToken();
                            sentencas();
                            if (palavraReservada(token, "end")) {
                                token = lexico.nextToken();
                                // {A18}
                            } else {
                                System.out.println(token.getLinha() + "," + token.getColuna() + ", end esperado");
                            }
                        } else {
                            System.out.println(token.getLinha() + "," + token.getColuna() + ", (begin) esperado");
                        }
                    } else {
                        System.out.println(token.getLinha() + "," + token.getColuna() + ", (do) esperado");
                    }
                } else {
                    System.out.println(token.getLinha() + "," + token.getColuna() + ", ) esperado");
                }
            } else {
                System.out.println(token.getLinha() + "," + token.getColuna() + ", ( esperado");
            }
            // if ( <expressao_logica> ) {A19} then begin <sentencas> end {A20} <pfalsa>
            // {A21} |
        } else if (palavraReservada(token, "if")) {
            token = lexico.nextToken();
            if (token.getClasse() == Classe.cParEsq) {
                token = lexico.nextToken();
                expressao_logica();
                // {A19}
                if (token.getClasse() == Classe.cParDir) {
                    token = lexico.nextToken();
                    if (palavraReservada(token, "then")) {
                        token = lexico.nextToken();
                        if (palavraReservada(token, "begin")) {
                            token = lexico.nextToken();
                            sentencas();
                            // <sentencas> end {A20}
                            if (palavraReservada(token, "end")) {
                                token = lexico.nextToken();
                                // {A20}
                                pfalsa();
                                // {A21}
                            } else {
                                System.out.println(token.getLinha() + "," + token.getColuna() + ", end esperado");
                            }
                        } else {
                            System.out.println(token.getLinha() + "," + token.getColuna() + ", (then) esperado");
                        }
                    } else {
                        System.out.println(token.getLinha() + "," + token.getColuna() + ", (begin) esperado");
                    }
                } else {
                    System.out.println(token.getLinha() + "," + token.getColuna() + ", ) esperado");
                }
            }

            // <id> {A49} := <expressao> {A22}
        } else if (token.getClasse() == Classe.cId) {
            // {A49}
            token = lexico.nextToken();
            if (token.getClasse() == Classe.cAtribuicao) {
                token = lexico.nextToken();
                expressao();
                // {A22}
            } else {
                System.out.println(token.getLinha() + "," + token.getColuna() + ", (:=) esperado");
            }
        }

    }

    // <pfalsa> ::= else {A25} begin <sentencas> end | ε
    private void pfalsa() {
        if (palavraReservada(token, "else")) {
            token = lexico.nextToken();
            // {A25}
            if (palavraReservada(token, "begin")) {
                token = lexico.nextToken();
                sentencas();
                if (palavraReservada(token, "end")) {
                    token = lexico.nextToken();
                    // {A25}
                } else {
                    System.out.println(token.getLinha() + "," + token.getColuna() + ", end esperado");
                }
            } else {
                System.out.println(token.getLinha() + "," + token.getColuna() + ", begin esperado");
            }
        }
    }

    // <expressao_logica> ::= <termo_logico> <mais_expr_logica>
    private void expressao_logica() {
        termo_logico();
        mais_expr_logica();
    }

    // <mais_expr_logica> ::= or <termo_logico> <mais_expr_logica> {A26} | ε
    private void mais_expr_logica() {
        if (palavraReservada(token, "or")) {
            token = lexico.nextToken();
            termo_logico();
            mais_expr_logica();
            // {A26}
        }
    }

    // <termo_logico> ::= <fator_logico> <mais_termo_logico>
    private void termo_logico() {
        fator_logico();
        mais_termo_logico();
    }

    // <mais_termo_logico> ::= and <fator_logico> <mais_termo_logico> {A27} | ε
    private void mais_termo_logico() {
        if (palavraReservada(token, "and")) {
            token = lexico.nextToken();
            fator_logico();
            mais_termo_logico();
            // {A27}
        }
    }

    /*
     * <fator_logico> ::= <relacional> |
     * ( <expressao_logica> ) |
     * not <fator_logico> {A28} |
     * true {A29} |
     * false {A30}
     */
    private void fator_logico() {
        int cont = 0;
        if ((token.getClasse() == Classe.cId) || (token.getClasse() == Classe.cInt)
                || (token.getClasse() == Classe.cParEsq)) {
            relacional();
            cont++;
        }
        if ((token.getClasse() == Classe.cParEsq) && (cont == 0)) {
            cont++;
            token = lexico.nextToken();
            expressao_logica();
            if (token.getClasse() == Classe.cParDir) {
                token = lexico.nextToken();
            } else {
                System.out.println(token.getLinha() + "," + token.getColuna() + ", ) esperado");
            }
        } else if (palavraReservada(token, "not")) {
            cont++;
            token = lexico.nextToken();
            fator_logico();
            // {A28}
        } else if (palavraReservada(token, "true")) {
            cont++;
            token = lexico.nextToken();
            // {A29}
        } else if (palavraReservada(token, "false")) {
            cont++;
            token = lexico.nextToken();
            // {A30}
        }
        if (cont == 0) {
            System.out.println(token.getLinha() + "," + token.getColuna()
                    + ", (relacional), (, (not), (true), ou (false) esperado");
        }

    }

    /*
     * <relacional> ::= <expressao> = <expressao> {A31} |
     * <expressao> > <expressao> {A32} |
     * <expressao> >= <expressao> {A33} |
     * <expressao> < <expressao> {A34} |
     * <expressao> <= <expressao> {A35} |
     * <expressao> <> <expressao> {A36}
     */
    private void relacional() {
        expressao();
        if (token.getClasse() == Classe.cIgual) {
            token = lexico.nextToken();
            expressao();
            // {A31}
        } else if (token.getClasse() == Classe.cMaior) {
            token = lexico.nextToken();
            expressao();
            // {A32}
        } else if (token.getClasse() == Classe.cMaiorIgual) {
            token = lexico.nextToken();
            expressao();
            // {A33}
        } else if (token.getClasse() == Classe.cMenor) {
            token = lexico.nextToken();
            expressao();
            // {A34}
        } else if (token.getClasse() == Classe.cMenorIgual) {
            token = lexico.nextToken();
            expressao();
            // {A35}
        } else if (token.getClasse() == Classe.cDiferente) {
            token = lexico.nextToken();
            expressao();
            // {A36}
        } else {
            System.out.println(
                    token.getLinha() + "," + token.getColuna() + "(>), (>=), (<), (<=), (=), ou (<>) esperado");
        }
    }

    // <expressao> ::= <termo> <mais_expressao>
    private void expressao() {
        termo();
        mais_expressao();
    }

    /*
     * <mais_expressao> ::= + <termo> <mais_expressao> {A37} |
     * - <termo> <mais_expressao> {A38} | ε
     */
    private void mais_expressao() {
        if (token.getClasse() == Classe.cAdicao) {
            token = lexico.nextToken();
            termo();
            mais_expressao();
            // {A37}
        } else if (token.getClasse() == Classe.cSubtracao) {
            token = lexico.nextToken();
            termo();
            mais_expressao();
            // {A38}
        }
    }

    // <termo> ::= <fator> <mais_termo>
    private void termo() {
        fator();
        mais_termo();
    }

    /*
     * <mais_termo> ::= * <fator> <mais_termo> {A39} |
     * / <fator> <mais_termo> {A40} | ε
     */
    private void mais_termo() {
        if (token.getClasse() == Classe.cMultiplicacao) {
            token = lexico.nextToken();
            fator();
            mais_termo();
            // {A39}
        } else if (token.getClasse() == Classe.cDivisao) {
            token = lexico.nextToken();
            fator();
            mais_termo();
            // {A40}
        }
    }

    // <fator> ::= <id> {A55} | <intnum> {A41} | ( <expressao> )
    private void fator() {
        if (token.getClasse() == Classe.cId) {
            token = lexico.nextToken();
            // {A55}
        } else if (token.getClasse() == Classe.cInt) {
            token = lexico.nextToken();
            // {A41}
        } else if (token.getClasse() == Classe.cParEsq) {
            token = lexico.nextToken();
            expressao();
            if (token.getClasse() == Classe.cParDir) {
                token = lexico.nextToken();
            } else {
                System.out.println(token.getLinha() + "," + token.getColuna() + ", ) esperado");
            }
        } else {
            System.out.println(token.getLinha() + "," + token.getColuna() + ", ( ou (id) ou (int) esperado");
        }
    }

    private boolean palavraReservada(Token token, String palavra) {
        return token.getClasse() == Classe.cPalavraReservada && token.getValor().getTexto().equals(palavra);
    }

}