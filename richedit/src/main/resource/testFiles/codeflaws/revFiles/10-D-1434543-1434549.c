#include<stdio.h>
#include<string.h>
int main(int argc, char *argv[])
{
int a[505],b[505],la,lb,f[505],p[505];
int i,j,n,k,max;


   
  while
    (scanf("%d",&la)!=EOF)
  {
    for(i=0;i<la;i++)
     scanf("%d",&a[i]);
    scanf("%d",&lb);
    for(i=0;i<lb;i++)
     scanf("%d",&b[i]);
    memset(f,0,sizeof(f));
	for(i=0;i<=500;i++)
		p[i]=-1;
    for(i=1;i<=la;i++)
    {
     k=0;
     for(j=1;j<=lb;j++)
     {
      if(b[j-1]<a[i-1]&&f[j]>f[k])//b[j-1]<a[i-1]是递增的条件，f[j]>f[k]是存放到 j时，j前面的最长的子序列
       k=j;
      if(a[i-1]==b[j-1]&&f[k]>=f[j])//逐步优化，要掌握
	  {f[j]=f[k]+1;
	    p[j]=k;
	  }
     }
	}
    max=0;
	int t=1;
    for(i=1;i<=lb;i++)
    {
		if(max<f[i]) {max=f[i];t=i;}
    }
	if(max==0)
	{printf("0\n");continue;}
	int k=0;
	int d[501];

d[++k]=b[t-1];
while(1)
{
	t=p[t];
	if(t==0)
		break;
d[++k]=b[t-1];
	
	
		   
		
		
}
printf("%d\n",k);
for(i=k;i>1;i--)
printf("%d ",d[i]);
    printf("%d\n",d[1]);
    

  }

return 0;
}