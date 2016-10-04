$ ->
   $.get "/devices", (devices) ->
       $.each devices, (index, device) ->
           $('#devices').append $("<li>").html "<a href='/devices/" + device.id + "'>" + device.id + "</a>"

$ ->
   $.get "/users", (users) ->
       $.each users, (index, user) ->
           $('#users').append $("<li>").html "<a href='/users/" + user.id + "'>" + user.id + "</a>"