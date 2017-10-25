package swing;

import org.abego.treelayout.NodeExtentProvider;

public class TaskBoxNodeExtentProvider implements NodeExtentProvider<TaskBox> {

    @Override
    public double getWidth(TaskBox treeNode) {
        return treeNode.width;
    }

    @Override
    public double getHeight(TaskBox treeNode) {
        return treeNode.height;
    }
}