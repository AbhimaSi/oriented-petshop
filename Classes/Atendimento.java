public class Atendimento {

    private String id;
    private int data;
    private int hora;
    private String status;
    private String idPet;
    private int idFuncionario;

    // Construtor vazio
    public Atendimento() {
    }

    // Construtor completo
    public Atendimento(String id, int data, int hora, String status, String idPet, int idFuncionario) {
        this.id = id;
        this.data = data;
        this.hora = hora;
        this.status = status;
        this.idPet = idPet;
        this.idFuncionario = idFuncionario;
    }

    // Getters e Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getData() {
        return data;
    }

    public void setData(int data) {
        this.data = data;
    }

    public int getHora() {
        return hora;
    }

    public void setHora(int hora) {
        this.hora = hora;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getIdPet() {
        return idPet;
    }

    public void setIdPet(String idPet) {
        this.idPet = idPet;
    }

    public int getIdFuncionario() {
        return idFuncionario;
    }

    public void setIdFuncionario(int idFuncionario) {
        this.idFuncionario = idFuncionario;
    }
}