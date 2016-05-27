package edu.scu.suragys.assignment3;

/**
 * Created by suragys on 5/21/16.
 */
public interface  ItemTouchHelperAdapter  {

    boolean onItemMove(int fromPosition, int toPosition);

    void onItemDismiss( int position);

}
