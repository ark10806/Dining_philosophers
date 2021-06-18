import java.util.concurrent.Semaphore;
class Philosopher_room extends Thread{
	int id, loop;
	Semaphore lfork, rfork, room;
	Philosopher_room(int id, Semaphore lfork, Semaphore rfork, Semaphore room, int loop){
		this.id = id;
		this.lfork = lfork;
		this.rfork = rfork;
		this.room = room;
		this.loop = loop;
	}
	public void run(){
		try{
			for(int i=0; i<this.loop; i++){
				room.acquire();
				lfork.acquire();
				rfork.acquire();
				eating();
				lfork.release();
				rfork.release();
				room.release();
				thinking();
			}
		}catch(InterruptedException e){}
	}
	void eating(){
		System.out.println("[" + id + "] eating");
	}
	void thinking(){
		System.out.println("[" + id + "] thinking");
	}
}
				
class Philosopher_once extends Thread{
	int id, loop;
	Semaphore lfork, rfork, once;
	Philosopher_once(int id, Semaphore lfork, Semaphore rfork, Semaphore once, int loop){
		this.id = id;
		this.lfork = lfork;
		this.rfork = rfork;
		this.once = once;
		this.loop = loop;
	}
	public void run(){
		try{
			for(int i=0; i<this.loop; i++){
				once.acquire();
				lfork.acquire();
				rfork.acquire();
				once.release();
				eating();
				lfork.release();
				rfork.release();
				thinking();
			}
		}catch(InterruptedException e){}
	}
	void eating(){
		System.out.println("[" + id + "] eating");
	}
	void thinking(){
		System.out.println("[" + id + "] thinking");
	}
}

class Philosopher_acyclic extends Thread{
	int id, loop;
	Semaphore lfork, rfork;
	Philosopher_acyclic(int id, Semaphore lfork, Semaphore rfork, int loop){
		this.id = id;
		this.lfork = lfork;
		this.rfork = rfork;
		this.loop = loop;
	}
	public void run(){
		try{
			for(int i=0; i<this.loop; i++){
				if(id < 4){
					lfork.acquire();
					rfork.acquire();
				}else{
					rfork.acquire();
					lfork.acquire();
				}
				eating();
				lfork.release();
				rfork.release();
				thinking();
			}
		}catch(InterruptedException e){}
	}
	void eating(){
		System.out.println("[" + id + "] eating");
	}
	void thinking(){
		System.out.println("[" + id + "] thinking");
	}
}


class DiningPhilosophersTest{
	static final int num = 5;
	public static void main(String[] args){
		int i, loop;
		long th0, th1, th2;
		loop = 10000;
		Semaphore[] fork = new Semaphore[num];
		
		for(i=0; i<num; i++)
			fork[i] = new Semaphore(1);
		Semaphore room = new Semaphore(4);

		Philosopher_room[] phil_room = new Philosopher_room[num];
		for(i=0; i<num; i++)
			phil_room[i] = new Philosopher_room(i, fork[i], fork[(i+1)%num], room, loop);
		th0 = System.currentTimeMillis();
		for(i=0; i<num; i++){
			phil_room[i].start();
			try{
				phil_room[i].join();
			}catch(InterruptedException e){}
		}
		th0 = System.currentTimeMillis() - th0;


		for(i=0; i<num; i++)
			fork[i] = new Semaphore(1);
		Semaphore once = new Semaphore(1);

		Philosopher_once[] phil_once = new Philosopher_once[num];
		for(i=0; i<num; i++)
			phil_once[i] = new Philosopher_once(i, fork[i], fork[(i+1)%num], once, loop);
		th1 = System.currentTimeMillis();
		for(i=0; i<num; i++){
			phil_once[i].start();
			try{
				phil_once[i].join();
			}catch(InterruptedException e){}
		}
		th1 = System.currentTimeMillis() - th1;


		for(i=0; i<num; i++)
			fork[i] = new Semaphore(1);
		Philosopher_acyclic[] phil_ac = new Philosopher_acyclic[num];
		for(i=0; i<num; i++)
			phil_ac[i] = new Philosopher_acyclic(i, fork[i], fork[(i+1)%num], loop);

		th2 = System.currentTimeMillis();
		for(i=0; i<num; i++){
			phil_ac[i].start();
			try{
				phil_ac[i].join();
			}catch(InterruptedException e){}
		}
		th2 = System.currentTimeMillis() - th2;

		System.out.println("th0:" + th1 + "ms");
		System.out.println("th1:" + th1 + "ms");
		System.out.println("th2:" + th2 + "ms");
	}
}
