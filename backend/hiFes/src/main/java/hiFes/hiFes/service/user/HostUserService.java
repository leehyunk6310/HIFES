package hiFes.hiFes.service.user;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import hiFes.hiFes.domain.user.HostUser;
import hiFes.hiFes.dto.user.HostUserSignUpDto;
import hiFes.hiFes.repository.user.HostUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class HostUserService implements UserDetailsService {
    private final HostUserRepository hostUserRepository;

    public void signUp(HostUserSignUpDto hostUserSignUpDto, Map<String, Object> context){

        HostUser hostUser = HostUser.builder()
                .email((String) context.get("email"))
                .name((String) context.get("name"))
                .phoneNo(hostUserSignUpDto.getPhoneNo())
                .organization(hostUserSignUpDto.getOrganization())
                .orgNo(hostUserSignUpDto.getOrgNo())
                .orgCode(hostUserSignUpDto.getOrgCode())
                .build();

        hostUserRepository.save(hostUser);

        // 로그인 진행. normal이면 여기서 FCM 토큰도 받아야함
        //session.setAttribute("userId", context.get("email"));

    }

    public HostUser getByEmail(String email) {
        return hostUserRepository.findByEmail(email).orElse(null);
    }

    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        HostUser hostUser = hostUserRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("존재하지 않는 이메일"));


        return org.springframework.security.core.userdetails.User.builder()
                .username(hostUser.getEmail())
                .build();
    }



    public Map<String, Object> searchKakaoUser(String token) {

        String reqURL = "https://kapi.kakao.com/v2/user/me";
        Map<String, Object> context = new HashMap<>();

        try {
            URL url = new URL(reqURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();


            // access_token 전송
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            conn.setRequestProperty("Authorization", "Bearer " + token);

            //결과 코드가 200이면 성공
            int responseCode = conn.getResponseCode();
            System.out.println("responseCode : " + responseCode);

            //응답 메세지 읽기
            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line = "";
            String result = "";

            while ((line = br.readLine()) != null) {
                result += line;
            }
            System.out.println("response body : " + result);

            //Gson 라이브러리로 JSON파싱
            JsonParser parser = new JsonParser();
            JsonElement element = parser.parse(result);

            int id = element.getAsJsonObject().get("id").getAsInt();
            String email = element.getAsJsonObject().get("kakao_account").getAsJsonObject().get("email").getAsString();
            String name = element.getAsJsonObject().get("properties").getAsJsonObject().get("nickname").getAsString();

            System.out.println("id : " + id);
            System.out.println("email : " + email);
            System.out.println("name : " + name);

            // 이거 성공하면 카카오 적용...

            br.close();


            context.put("id", id);
            context.put("email", email);
            context.put("name", name);


        } catch (IOException e) {
            e.printStackTrace();
        }

        return context;
    }



}