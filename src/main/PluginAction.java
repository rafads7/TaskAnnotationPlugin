package main;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;

public class PluginAction extends AnAction {

    public PluginAction(){
        super("Plugin Action");
    }

    @Override
    public void actionPerformed(AnActionEvent anActionEvent) {
        MainSwing.main( null );
    }
}
