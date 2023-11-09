#!/bin/bash

task="trino"

# Khởi tạo một phiên làm việc mới trong tmux
tmux new-session -d -s $task

# Tạo cửa sổ (tab) 1 và SSH vào server 1
tmux send-keys -t $task:0.0 "vagrant ssh machine1" C-m

# Tạo cửa sổ (tab) 2 và SSH vào server 2
tmux split-window -h -t $task:0.0 "vagrant ssh machine2"

# Kích hoạt chế độ sync
tmux setw synchronize-panes on

# ctrb b :setw synchronize-panes off

# Kích hoạt chế độ xem (layout even-horizontal) cho các pane
#tmux select-layout even-horizontal

# Attach vào phiên làm việc
tmux attach-session -t $task