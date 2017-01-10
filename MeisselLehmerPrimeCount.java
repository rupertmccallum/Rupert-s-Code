import java.lang.*;

public class MeisselLehmerPrimeCount {

   public static boolean [] beensieved;
   public static long N;
   public static int a;
   public static int k;
   public static int [] primes;
   public static boolean timeflag;
   public static long t1;

   public static void main(String[] args) {

      N = Long.parseLong(args[0]);
      timeflag = false;
      if (args.length>1) {
         if (args[1].equals("-t")) timeflag=true;
      }
      if (timeflag) {
         t1 = System.currentTimeMillis();
      }
      System.out.println(pi(N));
      if (timeflag) System.out.println("Time: "+
         (System.currentTimeMillis() - t1));

   }

   public static long pi(long N) {

      if (N<1) { return 0; }
      long aa;
      aa = (long)Math.pow(N,1D/3D);
      if (((aa+1)*(aa+1))*(aa+1)<=N) aa++;
      a = (int)aa;
      primes = sieve(a);

      long b = Math.max((long)a*(long)a,(long)Math.pow(N,2D/3D));
      int blocks = (int)((b+a-1)/a);

      long primecount = 0;
      long [] pi = new long[a];
      long pi_of_sqrt_of_N=0;
      long sum = 0;

      for(int j=1;j<=blocks;j++){
         long u=N-(j-1)*a;
         int ub;
         if (u<a) ub=(int)u; else ub=a;
         long [] block = new long[ub];
         for(int i=0; i<ub; i++){
            block[i] = (long)(j-1)*(long)a+i+1;
         }
         pi = sieve(block,primes,primecount);
         if (((long)(j-1)*(long)a+1)<=(long)Math.sqrt(N) &&
            (long)Math.sqrt(N)<(long)(j-1)*(long)a+ub+1) {
            int pos = (int)((long)Math.sqrt(N)-((long)(j-1)*(long)a+
               1));
            pi_of_sqrt_of_N=pi[pos];
         }

         int interval_end =
            (int)(Math.min((long)Math.sqrt(N),N/((long)(j-1)*(long)a+
                1)));
         int interval_start =
            (int)Math.min(Math.max(N/((long)(j-1)*(long)a+ub+1),
            (long)a),
            (long)interval_end);
         int interval_length = interval_end - interval_start;
         int [] interval = new int[interval_length];
         if (interval_length>0) {
            for(int i=interval_start+1;
            i<=interval_end;i++){
               interval[i-interval_start-1]=i;
            }
         }
         int [] primes_in_interval;

         if (interval_length>0) {
            primes_in_interval=sieve(interval,primes,interval_length,
               (int)Math.sqrt(Math.sqrt(N)));

            if (primes_in_interval.length>0) {
               long delta=0;
               for(int i=0;i<primes_in_interval.length;i++){
                  long c=N/(long)primes_in_interval[i];
                  int pos=(int)(c-(long)(j-1)*(long)a-1);
                  delta=delta+pi[pos];
               }
               sum=sum+delta;
            }
         }

         primecount=pi[pi.length-1];
      }

      long p_2=
         (long)primes.length*((long)primes.length-1)/2
         -pi_of_sqrt_of_N*(pi_of_sqrt_of_N-1)/2+sum;

      int [] mu = new int[a];
      int [] f = new int[a];
      for(int i=0;i<a;i++) f[i]=0;

      for(int i=0;i<a;i++) mu[i]=1;
      for(int i=0;i<primes.length;i++) {
         for(int j=0;j<a;j++) {
            if (((j+1)%primes[i])==0) {
               mu[j]=mu[j]*-1;
               if (f[j]==0) f[j]=primes[i];
            }
         }
      }
      for(int i=0;i<primes.length;i++) {
         if (primes[i]>(int)Math.pow(N,0.1667)) break;
         for(int j=0;j<a;j++) {
            if ((j+1)%(primes[i]*primes[i])==0) mu[j]=0;
         }
      }

      long s_1=0;
      for(int i=1;i<=a;i++) s_1=s_1+mu[i-1]*(long)(N/i);

      long s_2=0;
      k=(int)(Math.log(a)/Math.log(2));
      int [][] c = new int[k+1][a+1];
      beensieved = new boolean[a];
      long [] phi = new long[primes.length+1];
      long ph;
      for(int i=0;i<primes.length+1;i++) phi[i]=0;

      for(int j=1;j<=blocks;j++){
         long u=N-((long)(j-1)*(long)a);
         int ub;
         if (u<a) ub=(int)u; else ub=a;
         long [] block = new long[ub];
         for(int i=0; i<ub; i++){
            block[i] = (long)(j-1)*(long)a+i+1;
            beensieved[i] = false;
         }
         for(int i=0;i<k+1;i++) {
            for(int l=0;l<=(int)(a>>i);l++) {
               c[i][l]=Math.min((int)(1<<i),
                  (int)(a-l*(1<<i)));
            }
         }

         for(int i=-1;i<primes.length;i++){
            if (i>-1) c=sieve(block,primes[i],c);
            if (i<primes.length-1) {
               int interval_end=
                  Math.min(a,(int)(N/
                  (((long)(j-1)*(long)a+1)*(long)primes[i+1])));
               int interval_start=
                  Math.min(a,(int)(N/
                  (((long)(j-1)*(long)a+ub+1)*(long)primes[i+1])));
               if (interval_end>interval_start) {
                  for(int m=interval_start+1;m<=interval_end;m++) {
                     if (f[m-1]>primes[i+1] && mu[m-1]!=0
                        && m*primes[i+1]!=a) {
                        int l=(int)((N/((long)m*(long)primes[i+1]))-
                           (long)(j-1)*(long)a);
                        ph=phi[i+1];
                        int [] e = binary_expansion(l);
                        int ss=0;
                        for(int r=0;r<e.length;r++) {
                           ss=0;
                           for(int s=0;s<r;s++) {
                              ss+=(int)Math.pow(2,e[s]-e[r]);
                           }
                           ph+=c[e[r]][ss];
                        }
                        s_2-=mu[m-1]*ph;
                     }
                  }
               }
            }
            int [] e = binary_expansion(a);
            for(int r=0;r<e.length;r++) {
               int ss=0;
               for(int s=0;s<r;s++) {
                  ss+=(int)(1<<(e[s]-e[r]));
               }
               phi[i+1]+=c[e[r]][ss];
            }
         }
      }

      long p=(long)primes.length-p_2+s_1+s_2-1;
      return p;
   }

   public static int [] sieve(int max) {
      int n=max+1;

      boolean [] numberpool = new boolean [n];

      if (n>1) numberpool[1]=false;

      if (n>2) {
         for (int i=2; i<n; i++){
            numberpool[i]=true;
         }
      }

      double limit;

      limit = Math.sqrt(n);
      int j=2;
      if(j+j<n) {
         for (int i=j+j; i<n; i=i+j){
            numberpool[i]=false;
         }
      }

      if (limit>=3) {
         for (j=3; j<=limit; j=j+2){
            if (numberpool[j]==true){
               for (int i=j+j; i<n; i=i+j){
                  numberpool[i]=false;
	       }
            }
         }
      }

      int count=0;

      if (n>2) {
         for (int i=2; i<n; i++){
            if (numberpool[i]==true){
               count++;
            }
         }
      }
      int [] output=new int[count];
      count=0;
      if (n>2) {
         for(int i=2;i<n;i++){
            if (numberpool[i]==true){
               output[count]=i;
               count++;
            }
         }
      }

      return output;
   }

   public static int [] sieve(int [] interval, int [] primes,
      int interval_length, int upperbound) {
      boolean [] isprime = new boolean [interval_length];
      for(int i=0;i<interval_length;i++) isprime[i]=true;
      if (interval[0]==1) isprime[0]=false;

      for(int i=0;i<primes.length;i++) {
         if (primes[i]>upperbound) break;
         int first=0;
         int remainder=interval[first]%primes[i];
         if (remainder>0) first=first+primes[i]-remainder;
         if (first<interval_length) {
            if (interval[first]==primes[i]) first=first+primes[i];
         }
         if (first<interval_length) {
            for(int j=first;j<interval_length;j=j+primes[i]) {
               isprime[j]=false;
            }
         }
      }
      int primecount=0;
      int count=0;
      for(int i=0;i<interval_length;i++) {
         if (isprime[i]) primecount++;
      }
      int [] primelist = new int[primecount];
      for(int i=0;i<interval_length;i++) {
         if (isprime[i]) {
            primelist[count]=interval[i];
            count++;
         }
      }
   return primelist;
   }

   public static long [] sieve(long [] block, int [] primes, long
      primecount) {

      boolean [] isprime = new boolean [block.length];
      for(int i=0;i<block.length;i++) isprime[i]=true;
      if (block[0]==1) isprime[0]=false;

      for(int i=0;i<primes.length;i++) {
         int first=0;
         int remainder=(int)(block[first]%(long)primes[i]);
         if (remainder>0) first=first+primes[i]-remainder;
         if (first<block.length) {
            if (block[first]==primes[i]) first=first+primes[i];
         }
         if (first<block.length) {
            for(int j=first;j<block.length;j=j+primes[i]) {
               isprime[j]=false;
            }
         }
      }
      long [] pi = new long[block.length];
      long count = primecount;
      for(int i=0;i<block.length;i++) {
         if (isprime[i]) count++;
         pi[i]=count;
      }
      return pi;
   }

   public static int [][] sieve(long [] block, int prime, int [][] c) {
      int first=0;
      int remainder=(int)(block[first]%(long)prime);
      if (remainder>0) first+=prime-remainder;
      if (first<block.length) {
         for(int i=first;i<block.length;i=i+prime) {
            if (beensieved[i]==false) {
               for(int j=0;j<=k;j++)
                  c[j][(int)(i>>j)]-=1;
            beensieved[i]=true;
            }
         }
      }
      return c;
   }

   public static int [] binary_expansion(int l) {
      int [] e = new
         int[(int)(Math.log(l)/Math.log(2))+1];
      int count=0;
      for(int r=(int)(Math.log(l)/Math.log(2));
         r>=0;r--)
         {
            if (l>=(int)(1<<r)) {
               e[count]=r;
               count++;
               l-=(int)(1<<r);
         }
      }
      int [] ee = new int[count];
      for (int r=0;r<count;r++) ee[r]=e[r];
      return ee;
   }
}
