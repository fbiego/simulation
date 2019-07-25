
import java.lang.Math;

class Queue
{
	public static final int limQ = 100;
	public static final int busy = 1;
	public static final int idle = 0;
	public static int nEtype, custDelayed, nRuns, nEvt, nQ, servStat;
	public static float aNumQ, aServStat, mInter, mServ, sTime, lEvent, totDelays;
	public static float [] arrTime = new float[limQ + 1];
	public static float [] nEvent = new float[3];
	
	public static void main ( String [] arguments)
	{
		
		System.out.println("");
		System.out.println("Single Queue Single Server System");
		System.out.println("");

		nEvt = 2;		// number of events
		mInter = 1.0f;	// mean interarrival time
		mServ = 0.5f;	//mean service time
		nRuns = 1000;	// number of customers
		
		System.out.println("Mean Inter-Arrival Time: "+ mInter);
		System.out.println("Mean Service Time: " + mServ);
		System.out.println("Number of runs: "+ nRuns);
		System.out.println("");
		
		init();
		while (custDelayed < nRuns){
			timing();
			updateTime();
			switch (nEtype){
				case 1:
					arrive();
					break;
				case 2:
					depart();
					break;
			}
		}
		System.out.println("");
		report();
		System.out.println("");
	}
	
	public static void init()
	{
		sTime = 0;
		servStat = idle;
		nQ = 0;
		lEvent = 0.0f;
		custDelayed = 0;
		totDelays = 0.0f;
		aNumQ = 0.0f;
		aServStat = 0.0f;
		nEvent[1] = sTime + expon(mInter);
		nEvent[2] = 1.0e+30f;
	}
	
	public static void timing()
	{
		int x;
		float minNextEvent = 1.0e+29f;
		nEtype = 0;
		
		for (x = 1; x <= nEvt; ++x) {
			if (nEvent[x] < minNextEvent) {
				minNextEvent = nEvent[x];
				nEtype = x;
			}
		}
		if (nEtype == 0){
			System.out.println("Event list empty at "+ sTime);
			System.exit(1);
		}
		sTime = minNextEvent;
	}
	
	public static void arrive()
	{
		float delay;
		nEvent[1] = sTime + expon(mInter);
		if (servStat == busy){
			++nQ;
			if (nQ > limQ){
				System.out.println("overflow of array at "+ sTime);
				System.exit(2); 
			}
			arrTime[nQ] = sTime;
		} else {
			delay = 0.0f;
			totDelays += delay;
			++custDelayed;
			servStat = busy;
			nEvent[2] = sTime + expon(mServ);
		}
	}
	
	public static void depart()
	{
		int x;
		float delay;
		if (nQ == 0){
			servStat = idle;
			nEvent[2] = 1.0e+30f;
		} else {
			--nQ;
			delay = sTime - arrTime[1];
			totDelays += delay;
			++custDelayed;
			nEvent[2] = sTime + expon(mServ);
			for ( x = 1; x < nQ; ++x){
				arrTime[x] = arrTime[x+1];
			}
		}
	}
	
	public static void report()
	{
		System.out.println("Average delay in queue: " + (totDelays/custDelayed));
		System.out.println("Average number of customers in queue: " + (aNumQ/sTime));
		System.out.println("Server busy proportion: "+ (aServStat/sTime));
		System.out.println("");
		System.out.println("Time simulation ended "+ sTime);
	}
	
	public static void updateTime()
	{
		float timeLastEvent;
		timeLastEvent = sTime - lEvent;
		lEvent = sTime;
		aNumQ += nQ * timeLastEvent;
		aServStat += servStat * timeLastEvent;
	}
	
	public static float expon(float mean)
	{
		return (float)(-mean * Math.log(lcgrand(1)));
	}
	
	public static final long MOD = 2147483647;
	public static final long M1 = 24112;
	public static final long M2 = 26143;
	public static long seeds[] =
	{
		1,
		1973272912, 281629770,  20006270,1280689831,2096730329,1933576050,
		913566091, 246780520,1363774876, 604901985,1511192140,1259851944,
		824064364, 150493284, 242708531,  75253171,1964472944,1202299975,
		233217322,1911216000, 726370533, 403498145, 993232223,1103205531,
		762430696,1922803170,1385516923,  76271663, 413682397, 726466604,
		336157058,1432650381,1120463904, 595778810, 877722890,1046574445,
		68911991,2088367019, 748545416, 622401386,2122378830, 640690903,
		1774806513,2132545692,2079249579,  78130110, 852776735,1187867272,
		1351423507,1645973084,1997049139, 922510944,2045512870, 898585771,
		243649545,1004818771, 773686062, 403188473, 372279877,1901633463,
		498067494,2087759558, 493157915, 597104727,1530940798,1814496276,
		536444882,1663153658, 855503735,  67784357,1432404475, 619691088,
		119025595, 880802310, 176192644,1116780070, 277854671,1366580350,
		1142483975,2026948561,1053920743, 786262391,1792203830,1494667770,
		1923011392,1433700034,1244184613,1147297105, 539712780,1545929719,
		190641742,1645390429, 264907697, 620389253,1502074852, 927711160,
		364849192,2049576050, 638580085, 547070247
	};
	
	public static float lcgrand(int str)
	{
		long z, lowprd, hi31;
		z = seeds[str];
		lowprd = (z & 65535) * M1;
		hi31 = (z >> 16) * M1 + (lowprd >> 16);
		z = ((lowprd & 65535) - MOD) + ((hi31 & 32767) << 16) + (hi31 >> 15);
		if (z < 0 ) {
			z += MOD;
		}
		lowprd = (z & 65535) * M2;
		hi31 = (z >> 16) * M2 + (lowprd >> 16);
		z = ((lowprd & 65535) - MOD) + ((hi31 & 32767) << 16) + (hi31 >> 15);
		if (z < 0 ) {
			z += MOD;
		}
		seeds[str] = z;
		return (z >> 7 | 1)/16777216.0f;
	}
	public static void lcgrandst (long zset, int str)
	{
		seeds[str] = zset;
	}
	public static long lcggrandgt(int str)
	{
		return seeds[str];
	}
}