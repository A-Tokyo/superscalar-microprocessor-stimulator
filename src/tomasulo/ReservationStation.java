package tomasulo;

public class ReservationStation {
	
	int Vj;
	int Vk;
	int Qj;
	int Qk;
	int Address;
	int cycle_time;
	int execution_left ;
	int destination_ROB;
	boolean busy;
	String op;
	String name_Instruction;
	int offset;
	int Reg_Value;

	public ReservationStation(String name , int cycles) {
		this.name_Instruction=name;
		this.cycle_time=cycles ;
		this.execution_left= this.cycle_time;
		this.busy = false;
		this.Address = 0;
		this.destination_ROB =0;
		this.Vj=-1;
		this.Vk=-1;
		this.op="";
	}
	
	public void Flush(){
		this.busy = false;
		this.Address = 0;
		this.destination_ROB =0;
		this.Vj=-1;
		this.Vk=-1;
		this.op="";
		this.execution_left= this.cycle_time;
	}

	public static void main(String[] args) {

	}

}
