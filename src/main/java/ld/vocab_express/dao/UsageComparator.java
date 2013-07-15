package ld.vocab_express.dao;

import java.util.Comparator;

public class UsageComparator implements Comparator<VElement>
{
    public int compare(VElement o1, VElement o2)
    {
    	int i1 = Integer.parseInt(o1.getUsage());
    	int i2 = Integer.parseInt(o2.getUsage());
        return 	i2 - i1;
    }
}