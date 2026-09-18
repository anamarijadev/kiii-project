create or replace view tour_revenue as
select c.id as tour_id,
    c.title,
    c.price,
    count(e.id) as reservations,
    c.price * count(e.id) as revenue
from tours c
         left join reservations e on e.tour_id = c.id
group by c.id, c.title, c.price;