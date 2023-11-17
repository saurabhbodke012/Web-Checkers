<!DOCTYPE html>

<head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"></meta>
  <meta http-equiv="refresh" content="10">

  <title>Web Checkers | ${title}</title>
  <link rel="stylesheet" type="text/css" href="/css/style.css">
</head>

<body>
<div class="page">

  <h1>Web Checkers | ${title}</h1>

  <!-- Provide a navigation bar -->
  <#include "nav-bar.ftl" />

  <div class="body">

    <!-- Provide a message to the user, if supplied. -->
    <#include "message.ftl" />

    <#if players??>
        <u>Current Online Players</u>
        <ul>
            <#list players as player>
                <#if !player.inGame>
                    <li><a href="/game?name=${player.name}" style="background-color:#ecffca;color:black;">${player.name} - PLAY</a></li>
                <#else>
                    <li><a href="/spectator/game?name=${player.name}" style="background-color:#ffcaec;color:black;">${player.name} - SPECTATE</a></li>
                </#if>
           </#list>
        </ul>
    <#else>
        <b>${numberOfLoggedInPlayers}</b>
    </#if>

  </div>

</div>
</body>

</html>
