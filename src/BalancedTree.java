/**
 * The {@code:BalancedTree} represents an 2-3 Tree with some additional feature
 * to it to be able of handling the Runtime demands.
 * The Balanced Tree has 6 public function:
 * 1 - {@code:insert} - insert a Node with a unique key and its value in O(log(n))
 * 2 - {@code:delete} - delete a Node O(log(n))
 * 3 - {@coed:search} - search a Node by it's key O(log(n))
 * 4 - {@code:rank} - find the statistical arrangement by a key O(log(n)).
 * 5 - {@code:select} - return the selected place in statistical arrangement O(log(n))
 * 6 - {@code:sumValuesInInterval} return the sum of a given interval.
 *  */

public class BalancedTree {

    /**Root of the tree is a Node*/
    private Node root;
    /*Number OF Leaf in the tree, helps in insertaion method*/
    private int TreeNumOfLeaf;

    /**Constructor for tree, default key and value is null*/
    public BalancedTree() {
        this.root = new Node(null, null);
        this.TreeNumOfLeaf = 0;
    }

    /**Helper Node dataType for {@code:BalanceTRee}, represents Node for the tree*/
    private class Node {
        private Key key;
        private Value value;
        private Node parent;
        private Node rChild;
        private Node mChild;
        private Node lChild;
        private int numOfLeaf;
        private int numOfChild;

        private Node(Key key, Value value) {
            this.key = key;
            this.value = value;
            this.numOfLeaf = 0;
            this.numOfChild = 0;
            this.parent = null;
            this.rChild = null;
            this.mChild = null;
            this.lChild = null;
        }
    }

    /**
     * @param newKey   : new Key for insertion, must have same data type as K(tree data type).
     * @param newValue : new Value for insertion, must have same data type as V(tree data type).
     *                 Method insert, copy the {@code newKey} and it's {@code newValue} to the tree in log(n)
     *                 time complexity (n is the number of elements store in the current tree).
     *                 Note : insert method assume that all key are unique.
     */
    public void insert(Key newKey, Value newValue) {
        Node z = new Node(newKey.createCopy(), newValue.createCopy());
        z.numOfLeaf = 1; //Leaf has 1 leaf "under" him.
        /*Extreme case, tree has 1 leaf or none (this case occur when the tree just been Initialized). */
        if (this.TreeNumOfLeaf < 2) {
            initializedInsert(z);
            return;
        }
        /*If the method got to here, the tree is already Initialized (has at least 2 leaf*/
        Node y = this.root;
        while (y.numOfChild != 0) {
            if (z.key.compareTo(y.lChild.key) < 0)
                y = y.lChild;
            else if (z.key.compareTo(y.mChild.key) < 0 || y.rChild == null)
                y = y.mChild;
            else
                y = y.rChild;
        }
        Node x = y.parent;
        Node w = insertAndSplit(x, z);
        while (x != this.root) {
            x = x.parent;
            if (w != null)
                w = insertAndSplit(x, w);
            else
                updateNode(x);
        }
        if (w != null) {
            Node s = new Node(null, null);
            setChildren(s, x, w, null);
            this.root = s;
        }
        TreeNumOfLeaf++;
    }

    /**
     * @param x : Node
     *          {@code updateNode} Method  update the key of the maximum key,number of descended, number of
     *          children and
     *          sum of  values in x subtree.
     *          Note : (only) {@code:x.mChild;x.rChild} can be null.
     */
    private void updateNode(Node x) {
        x.numOfLeaf = x.lChild.numOfLeaf;
        x.numOfChild = 1;
        x.value = x.lChild.value.createCopy();
        if (x.mChild != null) {
            x.value.addValue(x.mChild.value);
            x.key = x.mChild.key;
            x.numOfChild = 2;
            x.numOfLeaf += x.mChild.numOfLeaf;
        }
        if (x.rChild != null) {
            x.numOfLeaf += x.rChild.numOfLeaf;
            x.value.addValue(x.rChild.value);
            x.key = x.rChild.key;
            x.numOfChild = 3;
        }
    }

    /**
     * @param newLeaf : new Leaf to insert
     *                {@code:initializedInsert} method is responsible for insert a new leaf
     *                in the extreme case (number of leafs is less then two).
     */
    private void initializedInsert(Node newLeaf) {
        newLeaf.parent = this.root;
        this.root.value = newLeaf.value.createCopy();
        this.root.key = newLeaf.key;
        if (this.root.lChild == null) {
            this.root.lChild = newLeaf;
            this.TreeNumOfLeaf = 1;
        } else {
            this.root.value.addValue(this.root.lChild.value);
            if (newLeaf.key.compareTo(this.root.lChild.key) > 0) {
                this.root.mChild = newLeaf;
            } else {
                this.root.mChild = this.root.lChild;
                this.root.lChild = newLeaf;
                this.root.key = this.root.mChild.key;
            }
            this.TreeNumOfLeaf = 2;
        }
        this.root.numOfLeaf++;
        this.root.numOfChild++;
    }

    /**
     * @param key : key to delete (delete the Node with the same key).
     *            {@code:finalDeletion} method is responsible for deleting Node in extreme cases (when {@code:TreeNumOfLeaf}
     *            is below 3). If there is'nt any Node with the key the method does nothing.
     */
    private void finalDeletion(Key key) {
        if (TreeNumOfLeaf == 1 && this.root.lChild.key.compareTo(key) == 0) {
            this.root.lChild = null;
            this.root.numOfChild = 0;
            this.root.numOfLeaf = 0;
            TreeNumOfLeaf--;
            return;
        }
        if (TreeNumOfLeaf == 2) {
            if (this.root.lChild.key.compareTo(key) == 0) {
                this.root.lChild = this.root.mChild;
                this.root.mChild = null;
                this.root.value = this.root.lChild.value.createCopy();
                this.root.numOfLeaf = 1;
                this.root.numOfChild = 1;
                TreeNumOfLeaf--;
                return;
            }
            if (this.root.mChild.key.compareTo(key) == 0) {
                this.root.mChild = null;
                this.root.key = this.root.lChild.key;
                this.root.value = this.root.lChild.value.createCopy();
                this.root.numOfLeaf = 1;
                this.root.numOfChild = 1;
                TreeNumOfLeaf--;
            }
        }
    }

    /**
     * @param x : root Node in subtree.
     * @param l : left Node in subtree.
     * @param m : middle Node in subtree.
     * @param r : right Node in subtree.
     *          {@code:setChildren} method set l, m and r to be the left, middle and right children respectively, of x.
     *          Note : r and m are the only note that can be null.
     */
    private void setChildren(Node x, Node l, Node m, Node r) {
        x.lChild = l;
        x.mChild = m;
        x.rChild = r;
        x.lChild.parent = x;
        if (x.mChild != null)
            x.mChild.parent = x;
        if (x.rChild != null)
            x.rChild.parent = x;
        updateNode(x);
    }

    /**
     * @param x : the root Node in subtree.
     * @param z : Node to be added.
     *          Insert node z as a child of node x, split x if necessary and return the new node (null
     *          if the method didn't split).
     */
    private Node insertAndSplit(Node x, Node z) {
        Node l = x.lChild;
        Node m = x.mChild;
        Node r = x.rChild;
        /*Dont need to split Node x*/
        if (x.numOfChild == 2) {
            if (z.key.compareTo(l.key) < 0)
                setChildren(x, z, l, m);
            else if (z.key.compareTo(m.key) < 0)
                setChildren(x, l, z, m);
            else setChildren(x, l, m, z);
            return null;
        }
        /*If the method got to here, split is needed*/
        Node y = new Node(null, null);
        if (z.key.compareTo(l.key) < 0) {
            setChildren(x, z, l, null);
            setChildren(y, m, r, null);
        } else if (z.key.compareTo(m.key) < 0) {
            setChildren(x, l, z, null);
            setChildren(y, m, r, null);
        } else if (z.key.compareTo(r.key) < 0) {
            setChildren(x, l, m, null);
            setChildren(y, z, r, null);
        } else {
            setChildren(x, l, m, null);
            setChildren(y, r, z, null);
        }
        return y;
    }

    /**
     * @param key : the key to delete
     *            {@code:delete} method will delete the Node with the desire key if
     *            the tree has a Node with the same key, if not, nothing will happens.
     */
    public void delete(Key key) {
        if (TreeNumOfLeaf == 0) //There are'nt any leaf to delete.
            return;

        /*finalDeletion take care of the Extreme case, Tree is'nt 2-3 Tree anymore */
        if (this.TreeNumOfLeaf < 3) {
            finalDeletion(key);
            return;
        }

        Node x = auxSearch(this.root, key);
        /*If the current key is not in the tree*/
        if (x == null)
            return;

        this.TreeNumOfLeaf--;
        Node y = x.parent;
        if (x == y.lChild)
            setChildren(y, y.mChild, y.rChild, null);
        else if (x == y.mChild)
            setChildren(y, y.lChild, y.rChild, null);
        else
            setChildren(y, y.lChild, y.mChild, null);
        while (y != null) {
            if (y.mChild == null) {
                if (y != this.root)
                    y = borrowOrMerge(y);
                else {
                    this.root = y.lChild;
                    y.lChild.parent = null;
                    return;
                }
            } else {
                updateNode(y);
                y = y.parent;
            }
        }
    }

    /**
     * @param y : Node
     *          {@code:borrowOrMerge} method borrow a Node child from a sibling x of y or merge x and y.
     *          return a reference to the parent of y (and x).
     */
    private Node borrowOrMerge(Node y) {
        Node z = y.parent;
        if (y == z.lChild) {
            Node x = z.mChild;
            if (x.rChild != null) {
                setChildren(y, y.lChild, x.lChild, null);
                setChildren(x, x.mChild, x.rChild, null);
            } else {
                setChildren(x, y.lChild, x.lChild, x.mChild);
                setChildren(z, x, z.rChild, null);
            }
            return z;
        }
        if (y == z.mChild) {
            Node x = z.lChild;
            if (x.rChild != null) {
                setChildren(y, x.rChild, y.lChild, null);
                setChildren(x, x.lChild, x.mChild, null);
            } else {
                setChildren(x, x.lChild, x.mChild, y.lChild);
                setChildren(z, x, z.rChild, null);
            }
            return z;
        }
        Node x = z.mChild;
        if (x.rChild != null) {
            setChildren(y, x.rChild, y.lChild, null);
            setChildren(x, x.lChild, x.mChild, null);
        } else {
            setChildren(x, x.lChild, x.mChild, y.lChild);
            setChildren(z, z.lChild, x, null);
        }
        return z;
    }

    /**
     * If the key exists in the tree, finds the value of node with the given key
     *
     * @param key
     * @return value of node with key or null if the key does not exist in tree
     */
    public Value search(Key key) {
        //key is larger than largest key in data structure
        if (key.compareTo(this.root.key) > 0) {
            return null;
        }
        Node x = auxSearch(this.root, key);
        if (x == null)
            return null;
        return x.value.createCopy();
    }

    /**
     * Determines which child's subtree holds the key (if it exists) and moves to that child
     * When arrives at leaf, checks if leaf is the key. If so, returns value. If not, the key is not in tree
     * and returns null
     *
     * @param currNode
     * @param key
     * @return value of node with key or null if the key does not exist in tree
     */
    private Node auxSearch(Node currNode, Key key) {
        if (currNode.lChild == null) {
            if (currNode.key.compareTo(key) == 0)
                return currNode;
            else
                return null;
        }

        //navigate to appropriate internal node
        if (key.compareTo(currNode.lChild.key) <= 0)
            return auxSearch(currNode.lChild, key);
        else if (key.compareTo(currNode.mChild.key) <= 0)
            return auxSearch(currNode.mChild, key);
        else //because we checked at root if key is inside data table there will always be right child
            return auxSearch(currNode.rChild, key);
    }

    /**
     * Finds the statistic order of node with given key, if the key is found in the tree
     *
     * @param key
     * @return integral representing placement of node in linear ordering of all nodes
     */
    public int rank(Key key) {
        //check if the key is larger than all the keys in the data structure
        if (key.compareTo(this.root.key) > 0)
            return 0;

        //search data structure for key and linear ordering
        return auxRank(this.root, key, 1);
    }

    /**
     * Given a node and tree, determines which child's subtree holds key (if exists).
     * and keeps track of the number of children "to the left" of the given key.
     *
     * @param currNode
     * @param key
     * @param currRank
     * @return integral representing placement of node in linear ordering of all nodes
     */
    private int auxRank(Node currNode, Key key, int currRank) {
        //if at leaf
        if (currNode.lChild == null) {
            if (currNode.key.compareTo(key) == 0) {
                return currRank;
            }
            //if the key is not in the data structure
            else
                return 0;
        }
        //navigate to appropriate node and count the number of leaves to the left of next node
        if (key.compareTo(currNode.lChild.key) <= 0) {
            return auxRank(currNode.lChild, key, currRank);
        } else if (key.compareTo(currNode.mChild.key) <= 0) {
            currRank += currNode.lChild.numOfLeaf;
            return auxRank(currNode.mChild, key, currRank);
        } else {//because we checked at root if key is inside data table there will always be right child
            currRank += currNode.lChild.numOfLeaf + currNode.mChild.numOfLeaf;
            return auxRank(currNode.rChild, key, currRank);
        }
    }

    /**
     * Given a number representing placement in linear ordering of node (index), returns the key of the node
     * which is found at the given placement.
     *
     * @param index
     * @return key of node found at index or null no node is at index
     */
    public Key select(int index) {
        // if the tree has one node and the index requested is one
        if ((index == 1) && (this.root.numOfLeaf == 1))
            return this.root.key;
        //check if the index is larger than the total amount of leaves or smaller then 1
        if ((index > this.root.numOfLeaf) || index < 1)
            return null;

        return auxSelect(this.root, index);
    }

    /**
     * Given a node and index, determines which child's subtree will contain the node at the index.
     *
     * @param currNode
     * @param index
     * @return key of node at index
     */
    private Key auxSelect(Node currNode, int index) {
        //if arrived at leaf return key
        if (currNode.lChild == null)
            return currNode.key.createCopy();

        //if at internal node, check to which child to continue
        if (index <= currNode.lChild.numOfLeaf)
            return auxSelect(currNode.lChild, index);
        else {
            index -= currNode.lChild.numOfLeaf; //subtract from index the num of descendents in the left child sub-tree
            if (index <= currNode.mChild.numOfLeaf)
                return auxSelect(currNode.mChild, index);
            else {
                index -= currNode.mChild.numOfLeaf; //subtract from index the num of descendents in the mid child sub-tree
                return auxSelect(currNode.rChild, index);
            }
        }
    }

    /**
     * Given two keys, finds sum of values belonging to nodes whose keys are found in the interval [key1, key2]
     *
     * @param key1
     * @param key2
     * @return value representing sum or null if no keys are found in that interval
     */
    public Value sumValuesInInterval(Key key1, Key key2) {
        //check if key1 is smaller/equal to key2
        if (key1.compareTo(key2) > 0)
            return null;

        Key smallestKey = select(1);
        //if the tree is empty
        if (smallestKey == null)
            return null;
        //check if the range ends before the smallest key
        if (key2.compareTo(smallestKey) < 0)
            return null;
        Node successor1 = findSuccessor(key1);
        //check if the range begins after the largest key
        if (successor1 == null)
            return null;

        //check if the upper bound is smaller than the key the succeeds key1. If so, the range is empty
        if (key2.compareTo(successor1.key) < 0)
            return null;

        Value sum = successor1.value.createCopy();

        if (root.numOfChild == 1) //if the tree only has one node
            return sum;
        else { //if the tree has at least two
            ascendingAddition(successor1.parent, successor1, sum, key2);
            return sum;
        }
    }

    /**
     * For a given node, sum and upper bound,
     * the function adds the sum of all the subtrees to the right of the given node if they are smaller
     * then the upper bound.
     * If there is a subtree to the right of the given node which is larger then the upper bound
     * then the function descends into that subtree and adds to the sum the value of the leaves smaller
     * then the upper bound.
     *
     * @param currNode
     * @param prevNode
     * @param sum
     * @param upperBound
     */
    private void ascendingAddition(Node currNode, Node prevNode, Value sum, Key upperBound) {
        //check all of the subtrees to the right of the node that we ascended from, if they are in range
        if (currNode.lChild == prevNode) {//if we ascended from the left child
            if (currNode.mChild.key.compareTo(upperBound) <= 0) {
                sum.addValue(currNode.mChild.value);
                if (currNode.rChild != null) {
                    if (currNode.rChild.key.compareTo(upperBound) <= 0) {
                        sum.addValue(currNode.rChild.value);
                    } else {
                        descendingAddition(currNode.rChild, sum, upperBound);
                        return;
                    }
                }
            } else {
                descendingAddition(currNode.mChild, sum, upperBound);
                return;
            }
        }
        //ascended from middle child and there is a right child
        else if ((currNode.rChild != null) && (currNode.mChild == prevNode)) {
            if (currNode.rChild.key.compareTo(upperBound) <= 0) {
                sum.addValue(currNode.rChild.value);
            } else {
                descendingAddition(currNode.rChild, sum, upperBound);
                return;
            }
        }

        //continue up the tree to the parent if not at root
        if (currNode.parent == null) {
            return;
        } else
            ascendingAddition(currNode.parent, currNode, sum, upperBound);
    }

    /**
     * Given a node, sum and upper bound,
     * the function explores the subtree rooted at the given node for leaves whose keys
     * are smaller than the upper bound
     *
     * @param currNode
     * @param sum
     * @param upperBound
     */
    private void descendingAddition(Node currNode, Value sum, Key upperBound) {
        //if arrived at leaf, check if in range. If so add value. If not, stop descent
        if (currNode.lChild == null) {
            if (currNode.key.compareTo(upperBound) <= 0)
                sum.addValue(currNode.value);
            else
                return;
        }

        //from left to right, check if the children subtrees are in range, if so add their values to sum
        if (currNode.lChild.key.compareTo(upperBound) <= 0) {
            sum.addValue(currNode.lChild.value);
            if (currNode.mChild.key.compareTo(upperBound) <= 0) {
                sum.addValue(currNode.mChild.value);
                if (currNode.rChild != null) {
                    if (currNode.rChild.key.compareTo(upperBound) <= 0)
                        sum.addValue(currNode.rChild.value);
                    else {//descend into right child's subtree
                        descendingAddition(currNode.rChild, sum, upperBound);
                    }
                }
            } else {//descend into middle child's subtree
                descendingAddition(currNode.mChild, sum, upperBound);
            }
        } else {//descend into left child's subtree
            descendingAddition(currNode.lChild, sum, upperBound);
        }
    }

    private Node findSuccessor(Key key) {
        if (key.compareTo(this.root.key) > 0)
            return null;
        else
            return auxFindSuccessor(this.root, key);
    }

    private Node auxFindSuccessor(Node currNode, Key key) {
        if (currNode.lChild == null) {
            return currNode;
        }

        //navigate to appropriate internal node
        if (key.compareTo(currNode.lChild.key) <= 0)
            return auxFindSuccessor(currNode.lChild, key);
        else if (key.compareTo(currNode.mChild.key) <= 0)
            return auxFindSuccessor(currNode.mChild, key);
        else //because we checked at root if key is inside data table there will always be right child
            return auxFindSuccessor(currNode.rChild, key);
    }
}