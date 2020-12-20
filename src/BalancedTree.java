/*
* The {@code BalancedTree} represents an 2-3 Tree with some additional feature
* to it to be able of handling the Runtime demands.
* */

public class BalancedTree<K extends Key,V extends Value> {

    /*Root of the tree, Must be InternalNode*/
    private InternalNode<K,V> root ;
    /*Number OF Leaf in the tree, helps in insertaion method*/
    private int numOfLeaf;
    /*Constructor for tree, default key and value is null */
    public BalancedTree(){
     this.root = new InternalNode<K,V>(null,null);
     this.numOfLeaf = 0;
    }

    /*Helper Node dataType for {@code BalanceTRee}, represents Node for the tree( Can be an {@code InternalNode}
    or {@code Leaf}
    * */
    private class Node<K extends Key,V extends Value>{
        private K key;
        private V value;
        private Node<K,V> parent;
        private Node<K,V> rChild;
        private Node<K,V> mChild;
        private Node<K,V> lChild;
        private int numOfChildren;
        private Node(K key, V value){
            this.key = key;
            this.value = value;
            this.numOfChildren = 0;
            this.parent = null;
        }
    }
    /*Data type Internal Node of the tree*/
    private class InternalNode<K extends Key,V extends Value> extends Node{
        private InternalNode(K key, V value) {
            super(key,value);
        }
    }
    /*Data type Leaf of the tree*/
    private class Leaf<K extends Key,V extends Value> extends Node{
        private Leaf(K key, V value){
            super(key, value);
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
        Leaf<K,V> z = new Leaf<K,V>((K)newKey.createCopy(), (V)newValue.createCopy());
        /*Extreme case, tree has 1 leaf or none (this case occur when the tree just been Initialized). */
        if (this.numOfLeaf < 2){
           initializedInsert(z);
           return;
        }
        /*If the method got to here, the tree is already Initialized (has at least 2 leaf*/
        Node y = this.root;
        while (y.numOfChildren != 0){
            if (((Node)z).key.compareTo(y.lChild.key) < 0)
                y = y.lChild;
            else if (((Node)z).key.compareTo(y.mChild.key) < 0)
                y = y.mChild;
            else
                y = y.rChild;
        }
        Node x = y.parent;
        Node w = insertAndSplit((InternalNode)x,z);
        while (x != this.root){
            x = x.parent;
            if (w != null)
                w = insertAndSplit((InternalNode)x,w);
            else
                updateNode((InternalNode)x);
        }
        if (w != null){
            InternalNode<K,V> s = new InternalNode<K,V>(null,null);
            setChildren(s,x,w,null);
            this.root = s;
        }
        numOfLeaf++;
    }

    /**
     * @param nodeX : InternalNode
     * {@code updateNode} Method  update the key of the maximum key,number of children and
     *  sum of childern values in nodeX subtree.
     *  Note : (only) {@code nodeX.rChild} can be null.
     */
    private void updateNode(InternalNode<K,V> nodeX){
        Node x = (Node)nodeX;
        x.numOfChildren = (x.lChild.numOfChildren + 1) + (x.mChild.numOfChildren + 1);
        x.value = (V)x.lChild.value.createCopy();
        x.value.addValue(x.mChild.value);
        x.key = x.mChild.key;
        if (x.rChild != null){
            x.numOfChildren =+ (x.rChild.numOfChildren + 1);
            x.value.addValue(x.rChild.value);
            x.key = x.rChild.key;
        }
    }

    /**
     * @param newLeaf : new Leaf to insert
     * {@code:initializedInsert} method is responsible for insert a new leaf
     * in extreme case (number of leafs is less then two).
     */
    private void initializedInsert(Leaf<K,V> newLeaf){
        ((Node)newLeaf).parent = this.root;
        ((Node)this.root).value = (V)((Node)newLeaf).value.createCopy();
        ((Node)this.root).key = ((Node)newLeaf).key;
        if (((Node)this.root).lChild == null){
            ((Node)this.root).lChild = newLeaf;
            this.numOfLeaf = 1;
        }
        else {
            ((Node)this.root).value.addValue(((Node)this.root).lChild.value);
            if (((Node)newLeaf).key.compareTo(((Node)this.root).lChild.key) > 0){
                ((Node)this.root).mChild = newLeaf;
            }
            else {
                ((Node)this.root).mChild = ((Node)this.root).lChild;
                ((Node)this.root).lChild = newLeaf;
                ((Node)this.root).key = ((Node)this.root).mChild.key;
            }
            this.numOfLeaf = 2;
        }
        ((Node)this.root).numOfChildren++;
    }

    /**
     * @param x : root Node in subtree.
     * @param l : left Node in subtree.
     * @param m : middle Node in subtree.
     * @param r : right Node in subtree.
     * {@code:setChildren} method set l, m and r to be the left, middle and right children respectively, of x.
     * Note : r is the only note that can be null.
     */
    private void setChildren(InternalNode<K,V> x,Node<K,V> l,Node<K,V> m,Node<K,V> r){
        ((Node)x).lChild = l;
        ((Node)x).mChild = m;
        ((Node)x).rChild = r;
        updateNode(x);
    }

    /**
     * @param x : the root Node in subtree.
     * @param z : Node to be added.
     * Insert node z as a child of node x, split x if necessary and return the new node (null
     * if the method didn't split).
     */
    private InternalNode<K,V> insertAndSplit(InternalNode<K,V> x, Node<K,V> z) {
        Node<K,V> l = ((Node)x).lChild;
        Node<K,V> m = ((Node)x).mChild;
        Node<K,V> r = ((Node)x).rChild;
        /*Dont need to split Node x*/
        if (((Node)x).numOfChildren == 2){
            if (z.key.compareTo(l.key) < 0)
                setChildren(x,z,l,m);
            else if (z.key.compareTo(m.key) < 0)
                setChildren(x,l,z,m);
            else setChildren(x,l,m,z);
            return null;
        }
        /*If the method got to here, split is needed*/
        InternalNode<K,V> y = new InternalNode<K,V>(null,null);
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

    public Value search(Key key, Integer Rank) {
        Node currNode = this.root;


        //key is larger than largest key in data structure
        if(key.compareTo(currNode.key) > 0) {
            return null;
        }

        //check if the root is the key
        if(key.compareTo(currNode.key) == 0) {
            return currNode.value.createCopy();
        }



        while(currNode.lChild != null) {
            if(currNode.lChild.key.compareTo(key) >= 0)
                currNode = currNode.lChild;
            else if(currNode.mChild.key.compareTo(key) >= 0) {
                currNode = currNode.mChild;
                rank =+ currNode.lChild.numOfDesc;
            }
            else {//because the key is smaller than the rightmost leaf's key there will never be a case where the rChild is null
                currNode = currNode.rChild;
                rank =+ currNode.lChild.numOfDesc + currNode.mChild.numOfDesc;
            }
        }





    }
}
