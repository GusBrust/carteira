
class GerirContas {

    public static void main(String[] args) {
        Database db = Database.carregar();
        System.out.println(db);
        
        
        // Database db = new Database();

        Conta cc = new Conta("João Silva", 1500.0);
        Conta cp = new Conta("Maria Oliveira", 2000.0);
        System.out.println(cc.toString());
        System.out.println(cp.toString());

        db.removerConta(0);

        // db.adicionarConta(cc);
        // db.adicionarConta(cp);

        System.out.println(db);

    }

}
