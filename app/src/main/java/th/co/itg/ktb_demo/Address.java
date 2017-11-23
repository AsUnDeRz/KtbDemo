package th.co.itg.ktb_demo;

/**
 * Created by ToCHe on 23/11/2017 AD.
 */

public class Address {

    private String rawAddress;
    Address(String address) {
        this.rawAddress=address;
    }

    String getAddress(){
        if(getIndexSubArea() == -1){
            return rawAddress.substring(0,rawAddress.indexOf("แขวง")).trim();
        }else{
            return rawAddress.substring(0,getIndexSubArea()).trim();
        }
    }
    String getArea(){
        if(getIndexArea() == -1 ){
            int indexArea = rawAddress.indexOf("เขต");
            String prefixArea = rawAddress.substring(indexArea);//เขตบึงกุ่ม
            return prefixArea.substring(0,prefixArea.indexOf(" ")).substring(3);
        }else{
            int indexArea = getIndexArea();
            String prefixArea = rawAddress.substring(indexArea);//เขตบึงกุ่ม
            return prefixArea.substring(0,prefixArea.indexOf(" ")).substring(5);
        }
    }
    String getSubArea(){
        if(getIndexSubArea() == -1){
            int indexSubArea = rawAddress.indexOf("แขวง");
            String prefixSubArea = rawAddress.substring(indexSubArea);//แขวงนวลจันทร์
            return prefixSubArea.substring(0,prefixSubArea.indexOf(" ")).substring(4);
        }else {
            int indexSubArea = getIndexSubArea();
            String prefixSubArea = rawAddress.substring(indexSubArea);//แขวงนวลจันทร์
            return prefixSubArea.substring(0,prefixSubArea.indexOf(" ")).substring(4);
        }
    }

    String getProvince(){
        if (getIndexProvince() == -1){
            String[] split = rawAddress.split(" ");
            return split[split.length-1];
        }else{
            return rawAddress.substring(getIndexProvince()+7);
        }
    }

    private int getIndexSubArea() {
        return rawAddress.indexOf("ตำบล");
    }

    private int getIndexArea() {
        return rawAddress.indexOf("อำเภอ");
    }

    protected int getIndexProvince() {
        return rawAddress.indexOf("จังหวัด");
    }

    public String getRawAddress() {
        return rawAddress;
    }

    public void setRawAddress(String rawAddress) {
        this.rawAddress = rawAddress;
    }
}
