package models;

import javax.persistence.Entity;
import play.db.jpa.Model;

@Entity
public class Inquiry extends Model {
  public String subject;
  public String question;
  public String answer;

  public Inquiry(String subject, String question, String answer) {
    this.subject = subject;
    this.question = question;
    this.answer = answer;
  }

  public String toString() {
    return subject;
  }
}
