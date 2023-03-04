package oasisbot24.oasisapi.repository;

import oasisbot24.oasisapi.domain.Member;

import java.util.List;
import java.util.Optional;

public interface MemberRepository {
    Member save(Member member);
    Member updateByAdmin(Member member);
    Member updateByUser(String password, String phone, String nickName);
    void updateUserTypeByEmailVerification(String email);
    Optional<Member> findById(Long id);
    Optional<Member> findByEmail(String email);
    List<Member> findAll();
}
