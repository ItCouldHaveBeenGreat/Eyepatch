from django.http import HttpResponse

def start_game_session(request):
    return HttpResponse("start_game_session")
    
def make_decision(request):
    return HttpResponse("make_decision")
    
def end_session(request):
    return HttpResponse("end_game_session")