package com.mycompany.pmsys;


/**
 *
 * @author Aluno
 */
public class DadosFuncionarios {

    private Integer idFunc;
    private String nomeFunc;
    private String identificador;
    private Integer idSquad;
    private Integer idMaquina;
    private Integer idCargo;

    public DadosFuncionarios(Integer squad, Integer idMaquina, Integer idCargo, String nomeFunc, String identificador, Integer idFunc) {

        this.idCargo = idCargo;
        this.idFunc = idFunc;
        this.idMaquina = idMaquina;
        this.identificador = identificador;
        this.idSquad = squad;
        this.nomeFunc = nomeFunc;

    }

    public String getNomeFunc() {
        return nomeFunc;
    }

    public Integer getIdCargo() {
        return idCargo;
    }

    public Integer getIdFunc() {
        return idFunc;
    }

    public Integer getIdMaquina() {
        return idMaquina;
    }

    public Integer getIdSquad() {
        return idSquad;
    }

    public String getIdentificador() {
        return identificador;
    }

    
}
