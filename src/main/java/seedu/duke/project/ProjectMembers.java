package seedu.duke.project;

import com.github.cliftonlabs.json_simple.JsonArray;
import com.github.cliftonlabs.json_simple.Jsonable;
import seedu.duke.sprint.Member;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

public class ProjectMembers implements Jsonable {

    public ArrayList<Member> memberList;

    public ProjectMembers() {
        memberList = new ArrayList<>(100);
    }

    public int size() {
        return memberList.size();
    }

    public Member getMember(int id) {
        return memberList.get(id);
    }

    public void addMember(Member m) {
        memberList.add(m);
    }

    //    public void addMember(List<String> userId) {
    //        Member m;
    //        for (String s : userId) {
    //            if (memberList.contains(new Member(s))) {
    //                System.out.println("The user associated with " + s + " is already added to the project");
    //            } else {
    //                m = new Member(s);
    //                memberList.add(m);
    //                System.out.println("The user associated with " + s + " has been added");
    //            }
    //        }
    //    }

    //add comparator for removing object
    public void deleteMember(List<String> userId) {
        for (String s : userId) {
            if (memberList.contains(new Member(s))) {
                memberList.remove(new Member(s));
                System.out.println("The user associated with " + s + " has been removed from the project");
            } else {
                System.out.println("This member is not associated with this project: " + new Member(s).getUserId());
            }
        }
    }

    public boolean containMember(Member member) {
        return memberList.contains(member);
    }

    public void removeMember(Member member) {
        memberList.remove(member);
    }

    @Override
    public String toJson() {
        final StringWriter writeable = new StringWriter();
        try {
            this.toJson(writeable);
        } catch (IOException e) {
            System.out.println("[Error] Cannot convert this project to JSON");
            e.printStackTrace();
        }
        return writeable.toString();
    }

    @Override
    public void toJson(Writer writer) throws IOException {
        final JsonArray jMemberList = new JsonArray(memberList);
        jMemberList.toJson(writer);
    }
}
