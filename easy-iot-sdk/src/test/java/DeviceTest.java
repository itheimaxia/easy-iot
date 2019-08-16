import com.itheimaxia.easyiot.sdk.IotDevice;
import org.junit.Test;

public class DeviceTest {
    @Test
    public void testConnect(){
//        host: tcp://192.168.62.133:1883
//        clientid: mqttjs_e8022a4d0b
//        topic: good,test,yes
//        username: xiadong/U2arIT
//        password: TvxY1Q
        IotDevice device = new IotDevice();
        device.setClientId("mqttjs_e8022a4d0b");
        device.setHost("tcp://192.168.62.133:1883");
        device.setProduct_name("xiadong");
        device.setDevice_name("U2arIT");
        device.setSecret("TvxY1Q");
        device.connect();
        device.subscribe("test");
        try {
            device.publish("test","aaaaa");
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
