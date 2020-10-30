import java.util.*;
public class MergeSort{
	public void merge_sort(int[] A, int l, int r) {
		//Base case when l == r, it means now array only has one element.
		//Otherwise, we can splitting array in half, and merge them in combine steps
		if(l < r) {
			int mid = (l+r)/2;
			merge_sort(A, l, mid);
			merge_sort(A, mid+1, r);
			merge(A, l, mid, r);
		}
	}

	public void merge(int[] A, int l, int m, int r) {
		int n1 = m - l + 1;
		int n2 = r - m;

		int[] arr1 = new int[n1];
		int[] arr2 = new int[n2];

		for(int i = 0; i < n1; i++)
			arr1[i] = A[l+i];

		for(int i = 0; i < n2; i++)
			arr2[i] = A[m+1+i];

		int i = 0;
		int j = 0;
		for(int k = l; k <= r; k++) {
			if(i == n1) {
				A[k] = arr2[j];
				j++;
			}

			else if(j == n2) {
				A[k] = arr1[i];
				i++;
			}

			else {
				if(arr1[i] <= arr2[j]) {
					A[k] = arr1[i];
					i++;
				}

				else {
					A[k] = arr2[j];
					j++;
				}
			}
		}

	}

	public static void main(String[] args) {
		Scanner in = new Scanner(System.in);
		MergeSort ms = new MergeSort();
		int n = in.nextInt();
		int[] A = new int[n];
		for(int i = 0; i < n; i++) {
			A[i] = in.nextInt();
			System.out.print(A[i] + " ");
		}

		System.out.println();

		ms.merge_sort(A, 0, A.length-1);
		for(int i = 0; i < n; i++) {
			System.out.print(A[i] + " ");
		}

		System.out.println();
	}
}