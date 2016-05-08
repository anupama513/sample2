<!DOCTYPE html>
<html lang="en">
<head>
  <title>Make my Trip Demo</title>
  <meta charset="utf-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <link rel="stylesheet" href="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/css/bootstrap.min.css">
  <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.2/jquery.min.js"></script>
  <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/js/bootstrap.min.js"></script>
  <script  src="requests.js"></script>
    <script  src="GlaceAjaxAction.js"></script>
    <link rel="stylesheet" href="styles.css">
</head>
<body onload=" javascript:searchData(); javascript:resetDefaults();" style="background-image: url('ignasi_pattern_s.png');">

<div class="container-fluid">
  <h1>Make MY Trip</h1>
  <p>This demonstrates the actual flight view</p>      
  <div class="row" style="padding-left:15px;padding-right:15px;">
    <div class="col-sm-6" style=" width:20%;" id="actionsWindow">
    
    <div class="list-group">
  		<a href="#" class="list-group-item active" id="all">All</a>
 		<a href="#" class="list-group-item" id="civil">Civil Airports</a>
  		<a href="#" class="list-group-item" id="milit">Military Airport</a>
  		<a href="#" class="list-group-item" id="sea_based">Sea Plane Base</a>
  		<a href="#" class="list-group-item" id="harb">Harbours</a>
	</div>
    </div>
    <div class="col-sm-6 borderleft" style="width:80%;" >
    <div class="form-group">
  		<input class="form-control autocomplete floatLeft"  placeholder="Enter code, city or country" id="searchBox" >
  		<button type="button" class="btn btn-warning floatRight"  id="searchBtn" onclick="javascript:searchData();"><span class="glyphicon glyphicon-search"></span> Search</button>
	</div>
    <div id="dataWindow"></div>
   
    </div>
    <div class ="floatRight" style="position: fixed;bottom:0;right:0">
     <ul class="pager">
  		<li><a href="#">Previous</a></li>
  		<li><a href="#">Next</a></li>
	</ul>
	</div>
  </div>
</div>
    
</body>
</html>
