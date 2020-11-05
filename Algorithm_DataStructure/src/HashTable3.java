/**
 * 해쉬 테이블의 가장 큰 문제는 충돌
 * 충돌이란 서로 다른 데이터가 동일한 주소에 저장되는 경우를 말함
 *
 * 해결법 1. Chaining 기법(open hashing)
 *  충돌이 일어나면 링크드 리스트 자료구조를 사용하여 바로 뒤에 추가한다.
 *  즉 해쉬테이블 1개에 값을 단일 객체가 아닌 링크드리스트 객체를 담게된다
 *  Ex) Hash<String, LinkedList<Person> >
*      그러나 리스트중 어떤 값이 원하는 값인지 모르기 때문에 key도 함께 저장한다.
 *
 * 해결법2. Linear Probing(Close Hashing)
 *  해쉬 테이블 내의 저장공간 안에서 해결하는 기법으로
 *  충돌 해당 주소부터 다음 주소의 전까지 빈공간에 저장
 *  해쉬는 데이터의 크기보다 크게 할당되기 때문에 가능능 * **/
public class HashTable3 {
    public static void main(String[] args) {
    String[][] hashTable = new String[6][6];
    save(hashTable,"Andy","0104734");
    save(hashTable,"Ando","0101234");
    save(hashTable,"Andy","111111");

    System.out.println(read(hashTable, "Andy"));
    System.out.println(read(hashTable, "AAAA"));

    }
    public static int makeKey(String str){
        return str.hashCode();
    }

    public static int hashMethod(int key){
        return key%5;
    }

    public static void save(String[][] hashTable,String data, String value){
        int index_key = makeKey(data);
        int addr = hashMethod(index_key);

        for(int i = 0 ; i< hashTable[addr].length; i+=2){
            //값이 없거나 덮어쓰기라면
            if(hashTable[addr][i] == null || hashTable[addr][i].equals(data) ){
                hashTable[addr][i] = data;
                hashTable[addr][i+1] = value;
                break;
            }
        }

    }

    public static String read(String[][] hashTable, String data){
        int index_key = makeKey(data);
        int addr = hashMethod(index_key);
        String result= null;

        for(int i = 0 ; i < hashTable[addr].length ; i+=2){
            try {
                if(hashTable[addr][i].equals(data)){
                    result = hashTable[addr][i+1];
                    break;
                }

            }catch (NullPointerException nullPointerException){
                return  null;
            }


        }
        return result;

    }
}
