package com.javaex.controller;

import java.io.IOException;
import java.util.List;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.javaex.dao.PhoneDao;
import com.javaex.vo.PersonVo;

@WebServlet("/pbc")//이 주소를 치면 이 안에 메소드 실행됨
public class PhonebookController extends HttpServlet {

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//접수받아서 해야 할 일 get방식으로 요청온건 여기서 처리 --컨트롤러
		System.out.println("PhonebookController");
		
		String act = request.getParameter("action"); //파라미터변수이름 action. 이거 꺼내서 뭘 요청했는지 알아야함
		System.out.println(act); //테스트용. 주소창에서 pbc?action=___라고쓰면 ___가출력되고있음
		
		if("list".equals(act)) { //사용자가 보내준 값이 리스트면.
			System.out.println("action=list");//pbc?action=list요청해서 제대로 list로왔는지 확인용
			PhoneDao phoneDao = new PhoneDao();
			List<PersonVo> personList = phoneDao.getPersonList();
			//System.out.println(personList.toString());
			
			//servlet 으로는 표현하기에 복잡하다 --> 같은 자바 기술인 jsp를 이용한다(html쓰기편한)
			//어트리뷰트로 데이터 request에 넣어주기
			request.setAttribute("pList", personList);
			
			//포워드 - ()안에 누구한테 시킬지 적어주는것
			RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/list.jsp");
			rd.forward(request, response); //request, response를 list.jsp에게 넘기겠다는 뜻
		
		}else if("writeForm".equals(act)) {
			System.out.println("action=writeForm");//writeForm요청해서 잘 오는지 확인용
			
			RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/writeForm.jsp");
			rd.forward(request, response);
			
		}else if("write".equals(act)) {
			System.out.println("action=write");
			
			//파라미터 3개의 값 꺼내온다.
			String name = request.getParameter("name");
			String hp = request.getParameter("hp");
			String company = request.getParameter("company");
			
			//vo로 만든다
			PersonVo personvo = new PersonVo(name, hp, company);
			System.out.println(personvo); //데이터 확인용
			
			//dao 메모리 올린다
			PhoneDao phoneDao = new PhoneDao();
			
			//dao.insert(vo)
			phoneDao.personInsert(personvo);
			
			//리다이렉트     (여기는 포워드보다 리다이렉트가 더 나음)
			response.sendRedirect("/phonebook2/pbc?action=list"); //리다이렉트 할 주소 잘 넣어주기
		
		}else if("delete".equals(act)) {
			System.out.println("action=delete");
			
			PhoneDao phoneDao = new PhoneDao();//dao 메모리에 올리기
			
			int pid = Integer.parseInt(request.getParameter("no"));//문자열 숫자로 바꾸기
			phoneDao.personDelete(pid);//dao의 delete메소드 사용해서 삭제
			
			response.sendRedirect("/phonebook2/pbc?action=list");//리다이렉트
		
		}else if("updateForm".equals(act)) {
			System.out.println("action=updateForm");
			
			int personId = Integer.parseInt(request.getParameter("no"));
			
			PhoneDao phoneDao = new PhoneDao(); 
			PersonVo getPerson = phoneDao.getPerson(personId); //getPerson메소드 사용해서 정보 가져오기
			
			//어트리뷰트로 request에 넣어주기
			request.setAttribute("getP", getPerson); 
			
			//포워드
			RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/updateForm.jsp");
			rd.forward(request, response);
			
		}else if("update".equals(act)) {
			System.out.println("action=update");
			
			PhoneDao phoneDao = new PhoneDao();
			
			//파라미터 값 받기
			String name = request.getParameter("name");
			String hp = request.getParameter("hp");
			String company = request.getParameter("company");
			int personId = Integer.parseInt(request.getParameter("pId"));
			
			//PersonVo 메모리 올리고 dao메소드 사용
			PersonVo pvo = new PersonVo(personId, name, hp, company);
			phoneDao.personUpdate(pvo);
			
			//리다이렉트
			response.sendRedirect("/phonebook2/pbc?action=list");
			
		}else {
			System.out.println("파라미터값 얻음");
		}
		
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//post방식으로 요청온거 doget한테 넘기는거
		doGet(request, response);
	}

}
