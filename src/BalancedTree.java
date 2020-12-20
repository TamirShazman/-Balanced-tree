/*
* The {@code BalancedTree} represents an 2-3 Tree with some additional feature
* to it to be able of handling the Runtime demands.
* */

public class BalancedTree<K extends Key,V extends Value> {

    /*Root of the tree, Must be InternalNode*/
    private Node<K,V> root ;
    /*Number OF Leaf in the tree, helps in insertaion method*/
    private int numOfLeaf;
    /*Constructor for tree, default key and value is null */
    public BalancedTree(){
     this.root = new Node<K,V>(null,null);
     this.numOfLeaf = 0;
    }

    /*Helper Node dataType for {@code BalanceTRee}, represents Node for the tree* */
    private class Node<K extends Key,V extends Value>{
        private K key;
        private V value;
        private Node<K,V> parent;
        private Node<K,V> rChild;
        private Node<K,V> mChild;
        private Node<K,V> lChild;
        private int numOfDesc;
        private int numOfChild;
        private Node(K key, V value){
            this.key = key;
            this.value = value;
            this.numOfDesc = 0;
            this.numOfChild = 0;
            this.parent = null;
            this.rChild = null;
            this.mChild = null;
            this.lChild = null;
        }
    }

    /**
     * @param newKey : new Key for insertion, must have same data type as K(tree data type).
     * @param newValue : new Value for insertion, must have same data type as V(tree data type).
     * Method insert, copy the {@code newKey} and it's {@code newValue} to the tree in log(n)
     * time complexity (n is the number of elements store in the current tree).
     * Note : insert method assume that all key are unique.
     */
    public void insert(K newKey,V newValue){
        Node<K,V> z = new Node<K,V>((K)newKey.createCopy(), (V)newValue.createCopy());
        /*Extreme case, tree has 1 leaf or none (this case occur when the tree just been Initialized). */
        if (this.numOfLeaf < 2){
           initializedInsert(z);
           return;
        }
        /*If the method got to here, the tree is already Initialized (has at least 2 leaf*/
        Node y = this.root;
        while (y.numOfChild != 0){
            if (z.key.compareTo(y.lChild.key) < 0)
                y = y.lChild;
            else if (z.key.compareTo(y.mChild.key) < 0 || y.rChild == null)
                y = y.mChild;
            else
                y = y.rChild;
        }
        Node x = y.parent;
        Node w = insertAndSplit(x,z);
        while (x != this.root){
            x = x.parent;
            if (w != null)
                w = insertAndSplit(x,w);
            else
                updateNode(x);
        }
        if (w != null){
            Node<K,V> s = new Node<K,V>(null,null);
            setChildren(s,x,w,null);
            this.root = s;
        }
        numOfLeaf++;
    }

    /**
     * @param x : Node
     * {@code updateNode} Method  update the key of the maximum key,number of descended, number of
     *  children and
     *  sum of  values in nodeX subtree.
     *  Note : (only) {@code nodeX.rChild} can be null.
     */
    private void updateNode(Node<K,V> x){
        x.numOfDesc = (x.lChild.numOfDesc + 1) + (x.mChild.numOfDesc + 1);
        x.numOfChild = 2;
        x.value = (V)x.lChild.value.createCopy();
        x.value.addValue(x.mChild.value);
        x.key = x.mChild.key;
        if (x.rChild != null){
            x.numOfDesc =+ (x.rChild.numOfDesc + 1);
            x.value.addValue(x.rChild.value);
            x.key = x.rChild.key;
            x.numOfChild++;
        }
    }

    /**
     * @param newLeaf : new Leaf to insert
     * {@code:initializedInsert} method is responsible for insert a new leaf
     * in the extreme case (number of leafs is less then two).
     */
    private void initializedInsert(Node<K,V> newLeaf){
        newLeaf.parent = this.root;
        this.root.value = (V)newLeaf.value.createCopy();
        this.root.key = newLeaf.key;
        if (this.root.lChild == null){
            this.root.lChild = newLeaf;
            this.numOfLeaf = 1;
        }
        else {
            this.root.value.addValue(this.root.lChild.value);
            if (newLeaf.key.compareTo(this.root.lChild.key) > 0){
                this.root.mChild = newLeaf;
            }
            else {
                this.root.mChild = ((Node)this.root).lChild;
                this.root.lChild = newLeaf;
                this.root.key = this.root.mChild.key;
            }
            this.numOfLeaf = 2;
        }
        this.root.numOfDesc++;
        this.root.numOfChild++;
    }

    /**
     * @param x : root Node in subtree.
     * @param l : left Node in subtree.
     * @param m : middle Node in subtree.
     * @param r : right Node in subtree.
     * {@code:setChildren} method set l, m and r to be the left, middle and right children respectively, of x.
     * Note : r is the only note that can be null.
     */
    private void setChildren(Node<K,V> x,Node<K,V> l,Node<K,V> m,Node<K,V> r){
        x.lChild = l;
        x.mChild = m;
        x.rChild = r;
        x.lChild.parent = x;
        x.mChild.parent = x;
        if (x.rChild != null)
        x.rChild.parent = x;
        updateNode(x);
    }

    /**
     * @param x : the root Node in subtree.
     * @param z : Node to be added.
     * Insert node z as a child of node x, split x if necessary and return the new node (null
     * if the method didn't split).
     */
    private Node<K,V> insertAndSplit(Node<K,V> x, Node<K,V> z) {
        Node<K,V> l = x.lChild;
        Node<K,V> m = x.mChild;
        Node<K,V> r = x.rChild;
        /*Dont need to split Node x*/
        if (x.numOfChild == 2){
            if (z.key.compareTo(l.key) < 0)
                setChildren(x,z,l,m);
            else if (z.key.compareTo(m.key) < 0)
                setChildren(x,l,z,m);
            else setChildren(x,l,m,z);
            return null;
        }
        /*If the method got to here, split is needed*/
        Node<K,V> y = new Node<K,V>(null,null);
        if (z.key.compareTo(l.key) < 0){
            setChildren(x,z,l,null);
            setChildren(y,m,r,null);
        }
        else if (z.key.compareTo(m.key) < 0){
            setChildren(x,l,z,null);
            setChildren(y,m,r,null);
        }
        else if (z.key.compareTo(r.key) < 0){
            setChildren(x,l,m,null);
            setChildren(y,z,r,null);
        }
        else {
            setChildren(x,l,m,null);
            setChildren(y,r,z,null);
        }
        return y;
    }


}
