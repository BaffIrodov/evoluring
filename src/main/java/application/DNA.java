package application;

public class DNA {
    public String dnaCode;
    public Integer dnaCursor;

    public DNA(String dnaCode, Integer dnaCursor) {
        this.dnaCode = dnaCode;
        this.dnaCursor = dnaCursor;
    }

    public void addDnaCode(String addingElement) {
        this.dnaCode += addingElement;
    }
}
