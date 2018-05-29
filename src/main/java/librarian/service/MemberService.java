package librarian.service;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.PieChart;
import librarian.domain.Member;
import librarian.repository.IssueRepository;
import librarian.repository.MemberRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class MemberService {
// ------------------------------ FIELDS ------------------------------

    private final IssueRepository issueRepository;
    private final MemberRepository memberRepository;

// --------------------------- CONSTRUCTORS ---------------------------

    public MemberService(IssueRepository issueRepository, MemberRepository memberRepository) {
        this.issueRepository = issueRepository;
        this.memberRepository = memberRepository;
    }

// -------------------------- OTHER METHODS --------------------------

    public void addMember(String name, String memberId, String mobile, String email) {
        Member member = new Member();
        member.setName(name);
        member.setEmail(email);
        member.setMemberId(memberId);
        member.setMobile(mobile);
        memberRepository.save(member);
    }

    public void deleteMember(String memberId) {
        memberRepository.deleteById(memberId);
    }

    @Transactional(readOnly = true)
    public boolean exists(String memberId) {
        return memberRepository.existsById(memberId);
    }

    @Transactional(readOnly = true)
    public Optional<Member> findById(String memberId) {
        return memberRepository.findById(memberId);
    }

    @Transactional(readOnly = true)
    public Optional<Member> getMember(String memberId) {
        return memberRepository.findById(memberId);
    }

    @Transactional(readOnly = true)
    public List<Member> getMembers() {
        return memberRepository.findAll();
    }

    @Transactional(readOnly = true)
    public boolean hasAnyBooks(String memberId) {
        return issueRepository.existsByMemberId(memberId);
    }

    @Transactional(readOnly = true)
    public ObservableList<PieChart.Data> memberGraphStatistics() {
        long memberCount = memberRepository.count();
        long issueCount = issueRepository.countByMemberId();

        ObservableList<PieChart.Data> data = FXCollections.observableArrayList();
        data.add(new PieChart.Data("Total Members (" + memberCount + ")", memberCount));
        data.add(new PieChart.Data("Active (" + issueCount + ")", issueCount));
        return data;
    }

    public void updateMember(String memberId, String name, String mobile, String email) {
        memberRepository.findById(memberId).ifPresent(member -> {
            member.setName(name);
            member.setMobile(mobile);
            member.setEmail(email);
            memberRepository.save(member);
        });
    }
}
