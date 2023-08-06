package ezen.ams.app;

import java.util.Scanner;

import ezen.ams.domain.Account;
import ezen.ams.domain.AccountRepository;
import ezen.ams.domain.MemoryAccountRepository;
import ezen.ams.domain.MinusAccount;
import ezen.ams.exception.NotBalanceException;

public class AMS {

	private static AccountRepository repository = new MemoryAccountRepository();
	private static Scanner scanner = new Scanner(System.in);

	public static void main(String[] args) {
		System.out.println("*****************************************");
		System.out.println("*****" + Account.BANK_NAME + "계좌 관리 어플리케이션******");
		System.out.println("*****************************************");

		// 가상데이터 임시등록

		repository.addAccount(new Account("김이젠", 1234, 100000000));
		repository.addAccount(new MinusAccount("박이젠", 1234, 100000000, 1000000));
		repository.addAccount(new Account("강이젠", 1234, 100000000));
		repository.addAccount(new MinusAccount("최이젠", 1111, 100000000, 2000000));

		boolean run = true;
		while (run) {
			System.out.println("------------------------------------------");
			System.out.println("1.계좌생성|2.계좌목록|3.입금|4.출금|5.종료");
			System.out.println("------------------------------------------");
			System.out.print("선택-> ");

			int selectNo = Integer.parseInt(scanner.nextLine());
			if (selectNo == 1) {
				// 계좌 생성 및 등록
				createAccount();
			} else if (selectNo == 2) {
				// 계좌목록
				showAccounts();
			} else if (selectNo == 3) {
				// 입금
				InputOutput(true);
			} else if (selectNo == 4) {
				// 출금
				InputOutput(false);
			} else if (selectNo == 5) {
				run = false;
			}
		}
		scanner.close();
		System.out.println("계좌 관리 애플리케이션을 종료합니다.");
	}

	/**
	 * 키보드(표준입력)로부터 계좌정보 입 력 받아 계좌 생성하기
	 */
	private static void createAccount() {
		System.out.print("일반 계좌는 1번, 마이너스 계좌는 2번을 눌러주세요 \t");
		int pm = Integer.parseInt(scanner.nextLine());
		Account account = inputAccount(pm);
		// AccountRepository에 계좌 등록
		repository.addAccount(account);
		System.out.println("# 계좌 등록이 정상 처리되었습니다.\n");
		System.out.println("현재 등록 된 계좌 수량 :" + repository.getCount() + "\n");
	}

//	계좌 기본정보 입력
	private static Account inputAccount(int type) {
		Account account = null;
		System.out.print("예금주명을 설정하세요: ");
		String owner = scanner.nextLine();

		System.out.print("비밀번호를 설정하세요: ");
		int passwd = Integer.parseInt(scanner.nextLine().trim());

		System.out.print("입금할 금액을 입력하세요: ");
		long inputMoney = Long.parseLong(scanner.nextLine());

		if (type == 1) {
			account = new Account(owner, passwd, inputMoney);
		} else if (type == 2) {
			System.out.print("대출 금액을 입력하세요: ");
			long borrowMoney = Long.parseLong(scanner.nextLine());
			account = new MinusAccount(owner, passwd, inputMoney, borrowMoney);
		}
		return account;
	}

//	계좌 조회 기능
	private static void showAccounts() {
		System.out.print("전체 계좌목록은 1번, 원하는 계좌 조회는 2번을 입력하세요.");
		int click = Integer.parseInt(scanner.nextLine());
		Account[] alist = repository.getAccounts();
		
		if(click == 1) {
		accountHeader();		
		for (int i = 0; i < repository.getCount(); i++) {
			// instanceof를 이용하여 일반,마이너스 계좌 구분하기
			if (alist[i] instanceof MinusAccount) {
				System.out.println("****************************************************************");
				System.out.println("마이너스계좌 \t" + alist[i].toString() + "");
			} else {
				System.out.println("****************************************************************");
				System.out.println("입출금계좌 \t" + alist[i].toString() + "");
			}
		}
		System.out.println("****************************************************************");
		System.out.println("\t감사합니다. 다음에 또 이용해 주세요. \n");
	
		
		}else if(click == 2) {
			System.out.println("조회하실 계좌번호를 입력 해 주세요");
		String num = scanner.nextLine();
			for (Account account : alist) {
				 if(account.getAccountNum().equals(num)) {
					 accountHeader();
					 System.out.println(account instanceof MinusAccount ? "마이너스계좌 \t" + account : "입출금계좌 \t" + account);return;
				 }  
			}
			System.out.println("조회하신 계좌가 없습니다."); 
		}
	}
	
	private static void accountHeader() {
		System.out.println("----------------------------------------------------------------");
		System.out.println("계좌 타입 | 계좌번호 | 성함 | 비밀번호 | 계좌잔액 | \t대출금");
		System.out.println("----------------------------------------------------------------");
	}

	/**
	 * 입금과 출금 하나로!
	 * 
	 * @param isDeposit
	 */
	private static void InputOutput(boolean isDeposit) {
		System.out.print((isDeposit ? "입금" : "출금") + "하실 계좌번호를 입력하세요: ");
		String accountNo = scanner.nextLine();
		Account account = repository.searchAccount(accountNo);
		if (account == null) {
			System.out.println("죄송합니다. 해당 계좌가 존재하지 않습니다.\n");
		} else {
			System.out.print((isDeposit ? "입금" : "출금") + "하실 금액을 입력하세요: ");
			long amount = Long.parseLong(scanner.nextLine());

			try {
				if (isDeposit) {
					account.deposit(amount);
				} else {
					account.withdraw(amount);
				}
			} catch (NotBalanceException e) {
				System.out.println(e.getMessage());
			}

			long restMoney = account.getRestMoney();
			System.out.println((isDeposit ? "입금" : "출금") + " 완료되었습니다.\n");
			System.out.println("*잔액은 " + restMoney + "원 입니다.*\n");
		}
		System.out.println("초기 화면으로 돌아갑니다.");
	}

	// 과거 입금과 출금기능으로 만들었던 메서드
	/*
	 * 입금메서드
	 * 
	 * private static void inputMoney() {
	 * System.out.print("입금을 원하시는 계좌번호를 입력하세요 :"); String no = scanner.nextLine();
	 * // 번호를 입력하면 그 계좌가 나온다. Account account = repository.searchAccount(no); if
	 * (account == null) { System.out.println("죄송합니다. 입금하실 계좌가 존재하지 않습니다.\n");
	 * 
	 * } else { System.out.print("입금하실 금액을 입력하세요: "); long inMoney =
	 * Long.parseLong(scanner.nextLine());
	 * 
	 * try { account.deposit(inMoney); } catch (NotBalanceException e) {
	 * System.out.println(e.getMessage()); }
	 * 
	 * long nn = account.getRestMoney();
	 * 
	 * System.out.println("입금 완료되었습니다.\n"); System.out.println("*잔액은" + nn +
	 * "원 입니다.*\n"); } System.out.println("초기 화면으로 돌아갑니다."); }
	 * 
	 * //출금 메서드 private static void outputMoney() {
	 * System.out.print("출금하실 계좌번호를 입력하세요 :"); String noo = scanner.nextLine();
	 * Account account = repository.searchAccount(noo); if (account == null) {
	 * System.out.println("죄송합니다. 출금하실 계좌가 존재하지 않습니다.");
	 * 
	 * } else if (account != null) { System.out.print("출금하실 금액을 입력하세요: "); long
	 * outMoney = Long.parseLong(scanner.nextLine());
	 * 
	 * try { account.withdraw(outMoney); } catch (NotBalanceException e) {
	 * System.out.println(e.getMessage()); }
	 * 
	 * long mm = account.getRestMoney(); System.out.println("출금 완료 되었습니다.\n");
	 * System.out.println("*잔액은" + mm + "원 입니다.*\n"); }
	 * System.out.println("초기 화면으로 돌아갑니다."); }
	 */
}
