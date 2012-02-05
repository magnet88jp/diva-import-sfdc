package models;

import javax.persistence.Entity;
import play.db.jpa.Model;

@Entity
public class Inquiry extends Model {
  public String code;
  public String subject;
  public String question;
  public String answer;

  public Inquiry(String code, String subject, String question, String answer) {
    this.code = code;
    this.subject = subject;
    this.question = question;
    this.answer = answer;
  }

  public String toString() {
    return subject;
  }
}
