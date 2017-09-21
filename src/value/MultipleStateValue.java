package value;

public class MultipleStateValue extends CustomValue
{
	public String[] noms;
	public MultipleStateValue(int s, String[] n)
	{
		super(n.length, s);
		this.noms = n;
	}
}
