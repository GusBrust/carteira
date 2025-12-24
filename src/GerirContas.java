
class GerirContas {

    public static void main(String[] args) {

        Database db = Database.carregar();
        System.out.println(db);
        

        // Conta contaCorrente = new Conta("Conta corrente", 1500.0);
        // Conta contaPoupanca    = new Conta("Conta poupança", 2000.0);
        // System.out.println(contaCorrente.toString());
        // System.out.println(contaPoupanca.toString());
        // try {
        // db.adicionarConta(contaCorrente);
        // } catch (IllegalArgumentException e) {
        //     System.out.println(e.getMessage());
        // }
        // try {
        //     db.adicionarConta(contaPoupanca);
        // } catch (IllegalArgumentException e) {
        //     System.out.println(e.getMessage());
        // }
        

        Despesa despesa = new Despesa(100.0, "Supermercado", new Categoria("Alimentação", "Gastos com comida", false), db.buscarContaPorNome("Conta corrente"));
        try {
            db.adicionarTransacao(despesa);
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
        Receita receita = new Receita(200.0, "Salário mensal", new Categoria("Salário", "Receita com salário", false), db.buscarContaPorNome("Conta corrente"));
        try {
            db.adicionarTransacao(receita);
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
        
        
        
        
        db.salvarTudo();
        System.out.println(db);

        // db.removerConta(0);

        // db.adicionarConta(cc);
        // db.adicionarConta(cp);

        // System.out.println(db);

    }

}
