import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BalancedTreeTest {

    class MyKey implements Key
    {
        private String str;
        private int identifier;

        @Override
        public String toString()
        {
            return "(" + str + "," + identifier + ")";
        }

        public MyKey(String str, int identifier)
        {
            this.str = str;
            this.identifier = identifier;
        }

        public MyKey(MyKey mykey)
        {
            this.str = mykey.str;
            this.identifier = mykey.identifier;
        }
        @Override
        public MyKey createCopy()
        {
            return new MyKey(this);
        }
        public void setKey(String str, int identifier)
        {
            this.str = str;
            this.identifier = identifier;
        }
        @Override
        public int compareTo(Key rhsKey)
        {
            if (this.str.compareTo(((MyKey) rhsKey).str) < 0)
            {
                return -1;
            }
            if (this.str.compareTo(((MyKey) rhsKey).str) > 0)
            {
                return 1;
            }
            if (this.identifier < ((MyKey) rhsKey).identifier)
            {
                return -1;
            }
            if (this.identifier > ((MyKey) rhsKey).identifier)
            {
                return 1;
            }
            return 0;
        }
    }

    class MyValue implements Value
    {
        private int value;

        public MyValue(int val)
        {
            this.value = val;
        }

        MyValue(MyValue myVal)
        {
            this.value = myVal.value;
        }
        @Override
        public MyValue createCopy()
        {
            return new MyValue(this);
        }
        @Override
        public void addValue(Value valueToAdd)
        {
            this.value = this.value + ((MyValue)valueToAdd).value;
        }
        @Override
        public String toString()
        {
            return String.valueOf(value);
        }
        public void setValue(int value)
        {
            this.value = value;
        }
    }
    @Test
    void insert() {
        BalancedTree T = new BalancedTree();

        MyKey[] myKeysArray = new MyKey[10];
        MyValue[] myValueArray = new MyValue[10];

        for(int i =0; i<10;i++)
        {
            myKeysArray[i] = new MyKey("b",i);
            myValueArray[i] = new MyValue(i);
            T.insert(myKeysArray[i],myValueArray[i]);
        }
    }
}