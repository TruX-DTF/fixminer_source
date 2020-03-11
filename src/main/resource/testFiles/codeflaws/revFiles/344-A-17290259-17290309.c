#include<stdio.h>
long int i,n,d,f,a[100005];
int main(int argc, char *argv[])
{
	scanf("%d",&n);
	
	for(i=0;i<n;i++)
	{
		scanf("%d",&a[i]);
	}
	d=1;f=1;
	for(i=0;i<n-1;i++)
	{
		if(a[i+1]==a[i])
		{
			d=d+1;
		}
		else{
			f=f+1;
		}
	}
	if(n==1||d==n)
	f=1;
	printf("%d",f);
	return 0;
}
