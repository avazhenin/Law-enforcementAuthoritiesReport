select con.name,
       con.date_of_birth,
       inv_customer_altel.get_registration_addr(ch.customer_id) registration_addr,
       inv_customer_altel.get_delivery_addr(ch.customer_id) delivery_addr,
       inv_customer_altel.get_documents(ch.customer_id) documents,
       con.inn,
       con.contract_num,
       p.msisdn
  from contract con
  join subs_history sh
    on (con.clnt_id = sh.clnt_id)
  join client_history ch
    on (sh.clnt_id = ch.clnt_id)
  join phone p
    on (p.phone_id = sh.phone_id)
 where sysdate between sh.stime and sh.etime - 1 / 86400
   and sysdate between ch.stime and ch.etime - 1 / 86400
   and sysdate between con.stime and con.etime - 1 / 86400