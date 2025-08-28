:- include('KB2.pl').

% Fetch initial state dynamically from the knowledge base 
initial_state(State) :-
    findall(BottleContents, dynamic_bottle_contents(BottleContents), State).

% Dynamically extract bottle contents
dynamic_bottle_contents(BottleContents) :-
    current_predicate(bottle1/2), % Check if bottle1/2 is defined
    call(bottle1(C1, C2)),        % Retrieve bottle1 contents
    parse_bottle([C1, C2], BottleContents).

dynamic_bottle_contents(BottleContents) :-
    current_predicate(bottle2/2), % Check if bottle2/2 is defined
    call(bottle2(C1, C2)),        % Retrieve bottle2 contents
    parse_bottle([C1, C2], BottleContents).

dynamic_bottle_contents(BottleContents) :-
    current_predicate(bottle3/2), % Check if bottle3/2 is defined
    call(bottle3(C1, C2)),        % Retrieve bottle3 contents
    parse_bottle([C1, C2], BottleContents).

% Parse bottle contents to handle empty layers
parse_bottle([C1, C2], Contents) :-
    (C1 = e, C2 = e -> Contents = [];       % Both layers empty -> []
     C1 = e -> Contents = [C2];             % Top layer empty -> [Bottom]
     C2 = e -> Contents = [C1];             % Bottom layer empty -> [Top]
     Contents = [C1, C2]).                  % Both layers filled


% Goal state: Each bottle contains one uniform color and is full
goal_state(State) :-
    maplist(is_uniform_and_full, State).

% Check if a bottle is uniform and full
is_uniform_and_full([Color, Color]). % Bottle contains two of the same color
is_uniform_and_full([]).             % Empty bottle is also considered valid

% Perform a pour action from bottle I to bottle J
pour(I, J, State, NewState) :-
    nth1(I, State, From),        % Get contents of bottle I
    nth1(J, State, To),          % Get contents of bottle J
    From = [Top | Rest],         % Bottle I has a top layer to pour
    (To = [] ; To = [Top | _]),  % Bottle J is empty or matches the top color
    length(To, Len), Len < 2,    % Bottle J has space for another layer
    replace(State, I, Rest, TempState), % Update bottle I
    replace(TempState, J, [Top | To], NewState).

% Replace Nth element in a list
replace([_|T], 1, X, [X|T]).
replace([H|T], N, X, [H|R]) :-
    N > 1, N1 is N - 1,
    replace(T, N1, X, R).

% Base situation
state(s0, State) :-
    initial_state(State).

% Result of applying an action to a state
state(result(pour(I, J), PrevSituation), NewState) :-
    state(PrevSituation, CurrentState),
    pour(I, J, CurrentState, NewState).

% Check if a situation achieves the goal state
goal(Situation) :-
    state(Situation, State),
    goal_state(State).