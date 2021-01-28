package model.Command;

public interface Command {

    public int execute(String[] arg) throws Exception;
    public void testArgs(String[] args) throws Exception;

}
