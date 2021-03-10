package mvc;
/* Edit history
   class made my Jesse 3/10
 */
public abstract class Model {

    private boolean unsavedChanges = false;
    private String fileName = null;

    public boolean getUnsavedChanges() {
        return unsavedChanges;
    }

    public String getFileName() {
        return fileName;
    }

    public void setUnsavedChanges(boolean b) {
        unsavedChanges = b;
    }
    public void setFileName(String fName) {
        fileName = fName;
    }

    public void changed(){
        unsavedChanges = true;
        firePropertyChange("changed", false, unsavedChanges);
    }
}
