package example.com.todolist;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by Lee on 29/8/2015.
 */
public class OnItemClickListenerListViewItem implements AdapterView.OnItemClickListener {

    //Cursor c;
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id){

        Context context = view.getContext();
         // c.moveToPosition(position);
       // int rowId = c.getInt(c.getColumnIndexOrThrow("_id"));
        TextView textViewItem = (TextView)view.findViewById(R.id.taskTextView);
        TextView timeViewItem = (TextView)view.findViewById(R.id.timeTextView);
        TextView dateViewItem = (TextView)view.findViewById(R.id.dateTextView);
        String listItemText = textViewItem.getText().toString();
       /* String listItemTime = timeViewItem.getText().toString();
        String listItemDate = dateViewItem.getText().toString();*/
        //String listItemId = textViewItem.getTag().toString();
        Toast.makeText(context, "Item: " + listItemText /*+ "Date: " + listItemDate + "Time: " +  listItemTime*/ , Toast.LENGTH_SHORT).show();
      //4/9/2015 update
        int id_To_Search = position +1;
        Bundle dataBundle = new Bundle();
        dataBundle.putInt("id", id_To_Search);
        Intent intent = new Intent(context, TaskAddingActivity.class);
        intent.putExtras(dataBundle);
        context.startActivity(intent);


    }
}

