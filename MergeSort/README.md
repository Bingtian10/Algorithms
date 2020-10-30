# Merge sort implementation

Implement the merge sort algorithm in CLRS book.

Basically we divide the given array into left half and right half recursively, and 
the loop invariant is that after the recursive call returns, the left half and 
right half are both sorted, all we need to do is to combine two sorted array
in O(n). Base case is when the halves only have 1 element, which is sorted by itself.

## Time complexity 

O(nlogn) The recurrence relationship is modeled by: T(n) = 2T(n/2) + O(n).
From master theorem and applying to case 2, we get O(nlogn).

## Space complexity

Also O(nlogn) as we have logn layers of recursion and we use total of O(n) space to combine
the result in each recursion layer.
