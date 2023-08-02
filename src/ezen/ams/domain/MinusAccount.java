package ezen.ams.domain;

public class MinusAccount extends Account {
	// 부모클래스에 없는 필드나 메소드 추가
	private long borrowMoney;

	public MinusAccount() {
//		 컴파일 시 부모클래스의 디폴트 생성자를 호출하는 super(); 자동 추가
		// super();
	}

	public MinusAccount(String accountOwner, int passwd, long restMoney, long borrowMoney) {
		// 여기서 super();로 디폴트 호출하면 안됨.(위에서 초기화 되어서)
		super(accountOwner, passwd, restMoney); // 부모 Account의 생성자를 이용해서 초기화할 때 사용
		this.borrowMoney = borrowMoney; // 자식 본인의 생성자
	}

//	메서드 추가(부모클래스에 없으니까)
	public long getBorrowMoney() {
		return borrowMoney;
	}

	public void setBorrowMoney(long borrowMoney) {
		this.borrowMoney = borrowMoney;
	}

	// 필요에 따라 부모클래스의 메소드 재정의 (OverRiding)
	@Override
	public long getRestMoney() {
		return super.getRestMoney() - borrowMoney;
	}
	// 그냥 restMoney는 부모클래스에서 private라 상속 불가. getRestMoney에 super를 붙여줌

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return super.toString() + "\t" + borrowMoney;
	}
}
