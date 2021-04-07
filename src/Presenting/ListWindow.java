package Presenting;

import jexer.TApplication;
import jexer.TList;
import jexer.TWidget;
import jexer.TWindow;
import jexer.event.TResizeEvent;

public class ListWindow extends TWindow {
    public final TList listField;

    public ListWindow(TApplication application, String title, int x, int y, int width, int height) {
        super(application, title, x, y, width, height);
        listField = this.addList(null, 0, 0, this.getWidth(), this.getHeight());
    }

    @Override
    public void onResize(final TResizeEvent event) {
        if (event.getType() == TResizeEvent.Type.WIDGET) {
            // Resize the text field
            TResizeEvent newSize = new TResizeEvent(event.getBackend(),
                    TResizeEvent.Type.WIDGET, event.getWidth(),
                    event.getHeight());
            listField.setHeight(newSize.getHeight());
            listField.setWidth(newSize.getWidth());
            return;
        }
        for (TWidget widget : getChildren()) {
            widget.onResize(event);
        }
    }
}
