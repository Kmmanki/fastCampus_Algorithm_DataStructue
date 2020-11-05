import java.util.ArrayList;
import java.util.List;

/**
 * 해시 테이블의 경우 키와 값으로 이루어진 자료구조
 * 1. 키를 받는다.
 * 2. 키를 해시 함수를 통해 주소를 얻는다.
 * 3. 얻은 주소로 접근하여 값을 얻는다.
 *
 * ps. 키를 만드는 알고리즘은 별도로 존재 할 수 있다.
 *
 *
 *
 * **/



//나머지 해시함수를 통한 간단한 해시 테이블 구현
public class HashTable {
    /**
     * 문자열을 받아 키를 만드는 메소드 작성
     * 키를 통해 주소를 배열의 주소(인덱스)를 받을 수 있는 해싱메소드 작성
     * 문자열을 사용하여 키를 만들고 키를 통해 인덱스 반환 하여 배열의 인덱스에 접근하여 저장
     *
     * **/

    public static void main(String[] args) {
        String[] hashTable = new String[6];

        int key = makeKey("Andy");
        int addr = hashMathod(key);
        int key2 = makeKey("Trump");
        int addr2 = hashMathod(key2);

        hashTable[addr] = "010-123-456";
        hashTable[addr2] ="010-333-777";

        System.out.println(hashTable[hashMathod(makeKey("Trump"))]);
    }

    //문자열의 0번째 아스키코드 반환
    public static int makeKey(String str){
        return str.charAt(0);
    }

    //키의 값을 나머지로 나누어 반환하는 해시
    public static int hashMathod(int key){
        return key%5;
    }
}

class HashTable2{
    public static void main(String[] args) {

    }
}