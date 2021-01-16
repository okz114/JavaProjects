package serialConnectUsb;

import java.io.File;

public class SerialUsbConnect
{
    static File[] oldListRoot = File.listRoots();

    public static void main(String[] args)
    {
        SerialUsbConnect.waitForNotifying();
    }

    public static void waitForNotifying()
    {
        if (File.listRoots().length > oldListRoot.length)
        {
            System.out.println("new drive detected");
            oldListRoot = File.listRoots();
            System.out.println("drive"+oldListRoot[oldListRoot.length-1]+" detected");
        }
        else if (File.listRoots().length < oldListRoot.length)
        {
            System.out.println(oldListRoot[oldListRoot.length-1]+" drive removed");
            oldListRoot = File.listRoots();
        }

    }

}
