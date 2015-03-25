package com.example.teamred.mpcremote;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.URL;


public class MainActivity extends ActionBarActivity {

    private Socket socket;
    private BufferedWriter out;
    private BufferedReader in;
    private String ip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        new Thread(new GetIP()).start();
    }

    /**************************************************
     * Function: Connect
     * Input: v - View to access the elements on the View
     * Description: Gets the IP address from the EditText to set up the socket connection
     * ************************************************/
    public void Connect(View v)
    {
        EditText ipText = (EditText)findViewById(R.id.ipText);
        ip = ipText.getText().toString();
        new Thread(new CreateSocket()).start();
    }

    class CreateSocket extends Thread
    {
        @Override
        public void run()
        {

            try
            {
                socket = new Socket(ip, 23621);
                out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }

        }

    }

    class GetIP extends Thread
    {
        @Override
        public void run()
        {

            try
            {
                URL url = new URL("http://glacial-fjord-1021.herokuapp.com/localservers");
                BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));

                String response = reader.readLine();
                EditText editText = (EditText)findViewById(R.id.ipText);
                editText.setText(response);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }

        }

    }

    /**************************************************
     * Function: PlayPause
     * Input: v - View to access the elements on the View
     * Description: Calls the Send method to send the command to server
     * ************************************************/
    public void PlayPause(View v)
    {
        Send(v, "Play/Pause");
    }

    /**************************************************
     * Function: Stop
     * Input: v - View to access the elements on the View
     * Description: Calls the Send method to send the command to server
     * ************************************************/
    public void Stop(View v)
    {
        Send(v, "Stop");
    }

    /**************************************************
     * Function: VolumeUp
     * Input: v - View to access the elements on the View
     * Description: Calls the Send method to send the command to server
     * ************************************************/
    public void VolumeUp(View v)
    {
        Send(v, "Volume Up");
    }

    /**************************************************
     * Function: VolumeDown
     * Input: v - View to access the elements on the View
     * Description: Calls the Send method to send the command to server
     * ************************************************/
    public void VolumeDown(View v)
    {
        Send(v, "Volume Down");
    }

    /**************************************************
     * Function: Next
     * Input: v - View to access the elements on the View
     * Description: Calls the Send method to send the command to server
     * ************************************************/
    public void Next(View v)
    {
        Send(v, "Next");
    }

    /**************************************************
     * Function: Previous
     * Input: v - View to access the elements on the View
     * Description: Calls the Send method to send the command to server
     * ************************************************/
    public void Previous(View v)
    {
        Send(v, "Previous");
    }

    /**************************************************
     * Function: Send
     * Input: v - View to access the elements on the View
     *        message - String of the message to send
     * Description: Attempts to create a socket connection and
     *              sends a message to the server then closes the connection
     * ************************************************/
    public void Send(View v, String message)
    {
        if(socket != null)
        {
            EditText ipText = (EditText)findViewById(R.id.ipText);
            ip = ipText.getText().toString();
            new Thread(new CreateSocket()).start();

            if (socket.isConnected())
            {
                try
                {
                    out.write(message);
                    out.flush();
                }
                catch (IOException io)
                {
                    io.printStackTrace();
                }
                finally
                {
                    try
                    {
                        if (socket.isConnected())
                        {
                            socket.close();
                        }
                    }
                    catch (IOException io)
                    {
                        io.printStackTrace();
                    }
                }
            }
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
